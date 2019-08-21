package com.example.api;

import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.IOTCAPIs;

public class AVAPIsClient {

    private static int sid; // tutk_platform_free session ID
    private static int uid; // 摄像头的uid
    private static final String username = "admin";
    private static final String password = "123456";
    private static int avIndex = -1; // avClientStart的返回值
    /**
     * 开始连接设备
     * @param uid
     */
    public static void start(String uid) {
        System.out.println("开始连接...");
        // 初始化IOTC(物联网)端，需在调用任何IOTC相关函数前调用次函数,此函数利用ip连接主机
        // 参数0代表随机选取UDP端口
        // 初始化成功返回常量 IOTC_ER_NoERROR
        int ret = IOTCAPIs.IOTC_Initialize2(0);
        System.out.println("IOTC_Initialize2 return = " + ret);
        if(ret != IOTCAPIs.IOTC_ER_NoERROR) {
            System.out.println("初始化失败...IOTCAPIs_Device可能已经存在");
            return ;
        }

        // 初始化 AV 模块
        // 调用 AV 模块函数前，必须初始化
        // 参数为 AV频道的最大数目
        AVAPIs.avInitialize(3);
        sid = IOTCAPIs.IOTC_Get_SessionID();

        if(sid < 0) {
            System.out.printf("IOTC_Get_SessionID error code [%d]\n", sid);
            return;
        }

        // 客户端将设备uid和tutk_platform_free session ID绑定,从而在物联网端连接设备
        ret = IOTCAPIs.IOTC_Connect_ByUID_Parallel(uid, sid);
        System.out.println("IOTC_Connect_ByUID_Parallel ret = " + ret);

        int[] servType = new int[1];
        // 接收AV数据前应通过AV服务器的认证
        avIndex = AVAPIs.avClientStart(sid, username, password, 20000, servType, 0);
        if (avIndex < 0) {
            System.out.printf("avClientStart 连接失败[%d]\n", avIndex);
            return;
        } else {
            System.out.println("avClientStart 连接成功 " + avIndex);
        }

        if (startIpcamStream(avIndex)) {
            Thread videoThread = new Thread(new VideoThread(avIndex),
                    "Video-Thread");
            Thread audioThread = new Thread(new AudioThread(avIndex),
                    "Audio-Thread");
            videoThread.start();
            audioThread.start();
            try {
                videoThread.join();
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
                return;
            }
            try {
                audioThread.join();
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        AVAPIs.avClientStop(avIndex);
        System.out.printf("avClientStop OK\n");
        IOTCAPIs.IOTC_Session_Close(sid);
        System.out.printf("IOTC_Session_Close OK\n");
        AVAPIs.avDeInitialize();
        IOTCAPIs.IOTC_DeInitialize();
        System.out.printf("StreamClient exit...\n");
    }

    // 用来判断是否和服务器建立了 IO 连接
    public static boolean startIpcamStream(int avIndex) {
        AVAPIs av = new AVAPIs();
        // 手机向服务端发送 IO 控制
        int ret = av.avSendIOCtrl(avIndex, AVAPIs.IOTYPE_INNER_SND_DATA_DELAY,
                new byte[2], 2);
        if (ret < 0) {
            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        // This IOTYPE constant and its corrsponsing data structure is defined in
        // Sample/Linux/Sample_AVAPIs/AVIOCTRLDEFs.h
        //
        int IOTYPE_USER_IPCAM_START = 0x1FF;
        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_START,
                new byte[8], 8);
        if (ret < 0) {
            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        int IOTYPE_USER_IPCAM_AUDIOSTART = 0x300;
        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_AUDIOSTART,
                new byte[8], 8);
        if (ret < 0) {
            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        return true;
    }
    /**
     * 关闭连接
     */
    public static void close() {
        AVAPIs.avClientStop(avIndex);
        System.out.println("avClientStop OK");
        IOTCAPIs.IOTC_Session_Close(sid);
        System.out.println("IOTC_Session_Close OK");
        AVAPIs.avDeInitialize();
        IOTCAPIs.IOTC_DeInitialize();
        System.out.printf("StreamClient exit...\n");
        AVAPIs.avClientStop(avIndex);
        System.out.printf("avClientStop OK\n");
        IOTCAPIs.IOTC_Session_Close(sid);
        System.out.printf("IOTC_Session_Close OK\n");
        AVAPIs.avDeInitialize();
        IOTCAPIs.IOTC_DeInitialize();
        System.out.printf("StreamClient exit...\n");
    }

    /**
     * 用来接收视频数据的线程
     * 一帧一帧接收
     */
    public static class VideoThread implements Runnable {
        static final int VIDEO_BUF_SIZE = 100000; // 预计视频buf大小
        static final int FRAME_INFO_SIZE = 16;  // 帧信息大小

        private int avIndex; // 需要传入的avIndex
        public VideoThread(int avIndex) {
            this.avIndex = avIndex;
        }

        @Override
        public void run() {
            System.out.printf("[%s] 开始接收视频\n",
                    Thread.currentThread().getName());

            AVAPIs av = new AVAPIs();
            byte[] frameInfo = new byte[FRAME_INFO_SIZE];
            byte[] videoBuffer = new byte[VIDEO_BUF_SIZE];  // 用来存取视频帧
            int[] outBufSize = new int[1];
            int[] outFrameSize = new int[1];
            int[] outFrmInfoBufSize = new int [1];
            while (true) {
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
                    }
                    catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                }
                else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                    System.out.printf("[%s] Lost video frame number[%d]\n",
                            Thread.currentThread().getName(), frameNumber[0]);
                    continue;
                }
                else if (ret == AVAPIs.AV_ER_INCOMPLETE_FRAME) {
                    System.out.printf("[%s] Incomplete video frame number[%d]\n",
                            Thread.currentThread().getName(), frameNumber[0]);
                    continue;
                }
                else if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {
                    System.out.printf("[%s] AV_ER_SESSION_CLOSE_BY_REMOTE\n",
                            Thread.currentThread().getName());
                    break;
                }
                else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {
                    System.out.printf("[%s] AV_ER_REMOTE_TIMEOUT_DISCONNECT\n",
                            Thread.currentThread().getName());
                    break;
                }
                else if (ret == AVAPIs.AV_ER_INVALID_SID) {
                    System.out.printf("[%s] Session cant be used anymore\n",
                            Thread.currentThread().getName());
                    break;
                }

                // Now the data is ready in videoBuffer[0 ... ret - 1]
                // Do something here
                // videoBuffer[0 ... ret - 1]为视频信息

            }

            System.out.printf("[%s] 退出\n",
                    Thread.currentThread().getName());
        }
    }
    // 获取音频的线程
    public static class AudioThread implements Runnable {
        static final int AUDIO_BUF_SIZE = 1024;
        static final int FRAME_INFO_SIZE = 16;

        private int avIndex;

        public AudioThread(int avIndex) {
            this.avIndex = avIndex;
        }

        @Override
        public void run() {
            System.out.printf("[%s] 开始获取音频\n",
                    Thread.currentThread().getName());

            AVAPIs av = new AVAPIs();
            byte[] frameInfo = new byte[FRAME_INFO_SIZE];
            byte[] audioBuffer = new byte[AUDIO_BUF_SIZE];
            while (true) {
                // ret用来判断是不是应该偶从缓冲区获取音频了，这样能保证音频质量
                int ret = av.avCheckAudioBuf(avIndex);

                if (ret < 0) {
                    // Same error codes as below
                    System.out.printf("[%s] avCheckAudioBuf() failed: %d\n",
                            Thread.currentThread().getName(), ret);
                    break;
                }
                else if (ret < 3) {
                    // 此时还不该获取，直接 sleep
                    try {
                        Thread.sleep(120);
                        continue;
                    }
                    catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                }

                int[] frameNumber = new int[1];
                // 开始获取音频数据，保存在audioBuffer中
                ret = av.avRecvAudioData(avIndex, audioBuffer,
                        AUDIO_BUF_SIZE, frameInfo, FRAME_INFO_SIZE,
                        frameNumber);

                if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {
                    System.out.printf("[%s] AV_ER_SESSION_CLOSE_BY_REMOTE\n",
                            Thread.currentThread().getName());
                    break;
                }
                else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {
                    System.out.printf("[%s] AV_ER_REMOTE_TIMEOUT_DISCONNECT\n",
                            Thread.currentThread().getName());
                    break;
                }
                else if (ret == AVAPIs.AV_ER_INVALID_SID) {
                    System.out.printf("[%s] Session cant be used anymore\n",
                            Thread.currentThread().getName());
                    break;
                }
                else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                    //System.out.printf("[%s] Audio frame losed\n",
                    //        Thread.currentThread().getName());
                    continue;
                }

                // Now the data is ready in audioBuffer[0 ... ret - 1]
                // Do something here
            }

            System.out.printf("[%s] 音频线程退出\n",
                    Thread.currentThread().getName());
        }
    }
}
