package com.example.api;

import com.tutk.IOTC.IOTCAPIs;

public class AVAPIsClient {

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
    }
}
