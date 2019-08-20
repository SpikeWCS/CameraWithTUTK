package com.example.camerawithtutk;


import android.app.Activity;

import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.IOTCAPIs;

public class Client {
    private Activity activity;
    private String uid;
    private int sid;
    private int avIndex = -1;

    public Client(Activity activity) {
        this.activity = activity;
    }

    public void init() {
        int ret = IOTCAPIs.IOTC_Initialize2(0);
        if (ret != IOTCAPIs.IOTC_ER_NoERROR) {
            System.out.printf("IOTCAPIs_Device exit…!!\n");
// Toast.makeText(activity,”IOTCAPIs_Device exit…!!\n”,Toast.LENGTH_SHORT).show();
            return;
        }
        AVAPIs.avInitialize(3);
        sid = IOTCAPIs.IOTC_Get_SessionID();
        if (sid < 0) {
            System.out.printf("IOTC_Get_SessionID error code [%d]\n", sid);
// Toast.makeText(activity,”IOTC_Get_SessionID error code [%d]\n”,Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void connet(String uid) {
        this.uid = uid;
        int a = IOTCAPIs.IOTC_Connect_ByUID_Parallel(uid, sid);
        System.out.printf("IOTC_Connect_ByUID_Parallel[%d]\n", a);
        int[] srvType = new int[1];
        String username = "admin";
        String password = "123456";
        System.out.printf(username + ":" + password + "\n");
        avIndex = AVAPIs.avClientStart(sid, username, password, 20000, srvType, 0);
        if (avIndex < 0) {
            System.out.printf("avClientStart failed[%d]\n", avIndex);
            // Toast.makeText(activity,”avClientStart failed[%d]\n”,Toast.LENGTH_SHORT).show();
            return;
        } else {
            System.out.printf("avClientStart connet\n", avIndex);
// Toast.makeText(activity,”avClientStart connet\n”,Toast.LENGTH_SHORT).show();
        }
    }

    public void cloes() {
        AVAPIs.avClientStop(avIndex);
        System.out.printf("avClientStop OK\n");
        IOTCAPIs.IOTC_Session_Close(sid);
        System.out.printf("IOTC_Session_Close OK\n");
        AVAPIs.avDeInitialize();
        IOTCAPIs.IOTC_DeInitialize();
        System.out.printf("StreamClient exit...\n");
    }
}
