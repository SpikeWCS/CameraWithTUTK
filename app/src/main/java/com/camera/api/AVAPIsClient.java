package com.camera.api;


import android.content.Context;
import android.widget.Toast;

import com.camera.camerawithtutk.AudioThread;
import com.camera.camerawithtutk.VideoThread;
import com.camera.model.User;
import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.IOTCAPIs;
import com.tutk.IOTC.Packet;

import java.util.Arrays;
import java.util.concurrent.BlockingDeque;

import es.dmoral.toasty.Toasty;

public class AVAPIsClient {

    private static int sid; // tutk_platform_free session ID
    private static String uid; // 摄像头的uid
    private static String username = "admin";
    private static String password = "123456";
    private static int avIndex = -1; // avClientStart的返回值
    private static Thread audioThread;
    private static Thread videoThread;
    /**
     * 修改视频清晰度的常量
     */
    public static byte nowQuality = -1;
    public static final byte AVIOCTRL_QUALITY_HIGH = 0x02;  // 640*480, 10fps, 256kbps  超清
    public static final byte AVIOCTRL_QUALITY_MIDDLE = 0x03;// 320*240, 15fps, 256kbps  高清
    public static final byte AVIOCTRL_QUALITY_LOW = 0x04;   // 320*240, 10fps, 128kbps  标清
    public static final byte AVIOCTRL_QUALITY_MIN = 0x05;   // 160*120, 10fps, 64kbps   流畅
    private static final int IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ = 0x0320;


    /**
     * 开始连接设备
     */
    public static void start(User user, BlockingDeque bq) {

        username = user.getUsername();
        password = user.getPassword();
        AVAPIsClient.uid =user.getUID();

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
        avIndex = AVAPIs.avClientStart(sid, "admin", password, 20000, servType, 0);
        AVAPIsClient.avIndex = avIndex;
        if (avIndex < 0) {
            System.out.printf("avClientStart 连接失败[%d]\n", avIndex);
            return;
        } else {
            System.out.println("avClientStart 连接成功 " + avIndex);
        }
        if (startIpcamStream(avIndex)) {
            startVideoThread(bq);
            try {
                videoThread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
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
    public static void startVideoThread(BlockingDeque bq) {
        if (startIpcamStream(avIndex)) {
            videoThread = new Thread(new VideoThread(avIndex,bq),
                    "Video-Thread");
            videoThread.start();
        }
    }

    public static void controlVideoThread() {
        videoThread.interrupt();
    }

    public static void startAudioThread() {
        if (startIpcamStream(avIndex)) {
            audioThread = new Thread(new AudioThread(avIndex),
                    "Audio-Thread");
        }
    }

    public static void controlAudioThread(int flag) {
        if (flag == 0) {
            audioThread.start();
        } else {
            audioThread.interrupt();
        }
    }
    public static class SMsgAVIoctrlSetStreamCtrlReq {
        int channel; // Camera Index
        byte quality; // AVIOCTRL_QUALITY_XXXX
        byte[] reserved = new byte[3];

        public static byte[] parseContent(int channel, byte quality) {

            byte[] result = new byte[8];
            byte[] ch = Packet.intToByteArray_Little(channel);
            System.arraycopy(ch, 0, result, 0, 4);
            result[4] = quality;

            return result;
        }
    }
    /**
     * 修改视频清晰度
     * @param qualityNum
     * qualityNum 0 - 3 依次为 流畅 吧标清 高清 超清
     */
    public static void setQuality(int qualityNum) {


        AVAPIs av = new AVAPIs();
        switch (qualityNum) {
            // 流畅
            case 0:
                if(nowQuality != AVIOCTRL_QUALITY_MIN) {
                    System.out.println("视频切换为流畅");
                    byte[] result = SMsgAVIoctrlSetStreamCtrlReq.parseContent(avIndex, AVIOCTRL_QUALITY_MIN);
                    int ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ,
                            result, 8);
                    if (ret < 0) {
                        System.out.printf("切换流畅失败 [%d]\n", ret);
                    }
                    nowQuality = AVIOCTRL_QUALITY_MIN;
                }

                break;
            // 标清
            case 1:
                if(nowQuality != AVIOCTRL_QUALITY_LOW) {
                    System.out.println("视频切换为标清");
                    byte[] result = SMsgAVIoctrlSetStreamCtrlReq.parseContent(avIndex, AVIOCTRL_QUALITY_LOW);
                    int ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ,
                            result, 8);
                    if (ret < 0) {
                        System.out.printf("切换标清失败 [%d]\n", ret);
                    }
                    nowQuality = AVIOCTRL_QUALITY_LOW;
                }
                break;
                // 高清
            case 2:
                if(nowQuality != AVIOCTRL_QUALITY_MIDDLE) {
                    System.out.println("视频切换为高清");
                    byte[] result = SMsgAVIoctrlSetStreamCtrlReq.parseContent(avIndex, AVIOCTRL_QUALITY_MIDDLE);
                    int ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ,
                            result, 8);
                    if (ret < 0) {
                        System.out.printf("切换高清失败 [%d]\n", ret);
                    }
                    nowQuality = AVIOCTRL_QUALITY_MIDDLE;
                }
                break;
            case 3:
                if(nowQuality != AVIOCTRL_QUALITY_HIGH) {
                    System.out.println("视频切换为超清");
                    byte[] result = SMsgAVIoctrlSetStreamCtrlReq.parseContent(avIndex, AVIOCTRL_QUALITY_HIGH);
                    int ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ,
                            result, 8);
                    if (ret < 0) {
                        System.out.printf("切换超清失败 [%d]\n", ret);
                    }
                    nowQuality = AVIOCTRL_QUALITY_HIGH;
                }
                break;
                default:
                    System.out.println("切换失败");
                    break;
        }
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
    }



    public static byte[] convertG711ToPCM(byte[] g711Buffer, int length, byte[] pcmBuffer) {
        System.out.println("g711Buf:" + Arrays.toString(g711Buffer));
        if (pcmBuffer == null) {
            pcmBuffer = new byte[length * 2];
        }
        for (int i = 0; i < length; i++) {
            byte alaw = g711Buffer[i];
            alaw ^= 0xD5;

            int sign = alaw & 0x80;
            int exponent = (alaw & 0x70) >> 4;
            int value = (alaw & 0x0F) >> 4 + 8;
            if (exponent != 0) {
                value += 0x0100;
            }
            if (exponent > 1) {
                value <<= (exponent - 1);
            }
            value = (char) ((sign == 0 ? value : -value) & 0xFFFF);
            pcmBuffer[i * 2] = (byte) (value & 0xFF);
            pcmBuffer[i * 2 + 1] = (byte) (value >> 8 & 0xFF);
        }
        System.out.println("PCM:" + Arrays.toString(pcmBuffer));
        return pcmBuffer;
    }

}

