package com.camera.model;

import java.util.ArrayList;

public class SaveFrames {

    public static ArrayList<Frames> frames = new ArrayList<>();

    public boolean canStart = false;

    //当全局变量等于StartReceive == true 时执行
    //将视频帧存入ArrayList中
    public void saveFrames(byte[] videoBuffer, byte[] frameInfo, int ret) {
        Frames frame = new Frames();
//                Map<Integer, Integer> map = new HashMap<>();
//                int save_x = 0;
//                byte[] sps, pps;
        int type = videoBuffer[4] & 0x1f;

        if (type == 7) {
            canStart = true;
        }

        // Now the data is ready in videoBuffer[0 ... ret - 1]
        // Do something here
        // videoBuffer[0 ... ret - 1]为视频信息
        if (canStart) {
//                    if (initXPS) {
//                        for (int i = 0; i < videoBuffer.length-3; i++) {
//                            if(videoBuffer[i]==0&&videoBuffer[i+1]==0&&videoBuffer[i+2]==0&&videoBuffer[i+3]==1){
//                                map.put(save_x,i);
//                                save_x++;
//                            }
//                        }
//                        int length_sps=map.get(1)-map.get(0);
//                        int offset_sps=map.get(0);
//                        sps=new byte[length_sps];
//                        System.arraycopy(videoBuffer,offset_sps,sps,0,length_sps);
//                        int length_pps=map.get(2)-map.get(1);
//                        int offset_pps=map.get(1);
//                        pps=new byte[length_pps];
//                        System.arraycopy(videoBuffer,offset_pps,pps,0,length_pps);
//                        System.out.print("SPS: ");
//                        for (int i = 0; i < sps.length; i++) {
//                            System.out.print(sps[i]+",");
//                        }
//                        System.out.println();
//                        System.out.print("PPS: ");
//                        for (int i = 0; i < pps.length; i++) {
//                            System.out.print(pps[i]+",");
//                        }
//                        System.out.println();
//                        initXPS = false;
//                    }
            boolean IFrame = false;
            if (frameInfo[2] == 1) {
                IFrame = true;
            }
            frame.setFrame(videoBuffer);
            frame.setIFrame(IFrame);
            frame.setSize(ret);
//            for (int ii = 0; ii < ret; ii++) {
//                System.out.print(videoBuffer[ii] + ",");
//            }
//            System.out.println();

            frames.add(frame);
        }
    }

    //当全局变量StartReceive == false 时执行
    //当ArrayList非空，执行Muxer后清空ArrayList
    public void stopReceive() {
        canStart = false;
        if (frames.size() != 0) {
            Muxer muxer = new Muxer();
            muxer.muxer(frames);
            frames.clear();
        }
    }
}
