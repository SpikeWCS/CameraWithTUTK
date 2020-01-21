package com.camera.camerawithtutk;


import com.camera.model.SaveFrames;
import com.decode.tools.BufferInfo;
import com.tutk.IOTC.AVAPIs;


import java.util.concurrent.BlockingDeque;

/**
 * 用来接收视频数据的线程
 * 一帧一帧接收
 */
public class VideoThread implements Runnable {
    static final int VIDEO_BUF_SIZE = 100000; // 预计视频buf大小
    static final int FRAME_INFO_SIZE = 16;// 帧信息大小

    private int avIndex; // 需要传入的avIndex
    //        private Handler  handler;
    private BlockingDeque bq;
    public static boolean startReceive = false;

    public VideoThread(int avIndex, BlockingDeque bq) {
        this.avIndex = avIndex;
//            this.handler = handler;
        this.bq = bq;
    }

    @Override
    public void run() {
        // 响应中断
        if (Thread.currentThread().isInterrupted()) {
            System.out.println("Java技术栈线程被中断，程序退出。");
            return;
        }
        System.out.printf("[%s] 开始接收视频\n",
                Thread.currentThread().getName());
        AVAPIs av = new AVAPIs();
        byte[] frameInfo = new byte[FRAME_INFO_SIZE];
        int[] outBufSize = new int[1];
        int[] outFrameSize = new int[1];
        int[] outFrmInfoBufSize = new int[1];
        SaveFrames saveFrames = new SaveFrames();
        while (true) {
            byte[] videoBuffer = new byte[VIDEO_BUF_SIZE];  // 用来存取视频帧
            int[] frameNumber = new int[1];
            // 返回结果为接收视频videoBuffer的实际长度
            int ret = av.avRecvFrameData2(avIndex, videoBuffer,
                    VIDEO_BUF_SIZE, outBufSize, outFrameSize,
                    frameInfo, FRAME_INFO_SIZE,
                    outFrmInfoBufSize, frameNumber);

            if (ret == AVAPIs.AV_ER_DATA_NOREADY) {
                try {
                    Thread.sleep(30);
                    continue;
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                System.out.printf("[%s] Lost video frame number[%d]\n",
                        Thread.currentThread().getName(), frameNumber[0]);
                continue;
            } else if (ret == AVAPIs.AV_ER_INCOMPLETE_FRAME) {
                System.out.printf("[%s] Incomplete video frame number[%d]\n",
                        Thread.currentThread().getName(), frameNumber[0]);
                continue;
            } else if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {
                System.out.printf("[%s] AV_ER_SESSION_CLOSE_BY_REMOTE\n",
                        Thread.currentThread().getName());
                break;
            } else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {
                System.out.printf("[%s] AV_ER_REMOTE_TIMEOUT_DISCONNECT\n",
                        Thread.currentThread().getName());
                break;
            } else if (ret == AVAPIs.AV_ER_INVALID_SID) {
                System.out.printf("[%s] Session cant be used anymore\n",
                        Thread.currentThread().getName());
                break;
            }



            // Now the data is ready in videoBuffer[0 ... ret - 1]
            //----------------------把videobuffer信息加入阻塞队列----------------------
            try {
                BufferInfo bi = new BufferInfo(outFrameSize[0], videoBuffer);
                bq.offer(bi);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //---------------------------------------------------------------------

            if (startReceive) {
                saveFrames.saveFrames(videoBuffer, frameInfo, ret);
            } else {
                saveFrames.stopReceive();
            }

        }


        System.out.printf("[%s] 退出\n",
                Thread.currentThread().getName());
    }
}