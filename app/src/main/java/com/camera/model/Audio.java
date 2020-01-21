package com.camera.model;

import java.util.Arrays;

public class Audio {
    private short[][] audioWithPCM;
    private final int MAX_AUDIO_LENGTH = 20000;
    private int audioLength;

    public Audio() {
        audioWithPCM = new short[MAX_AUDIO_LENGTH][];
        audioLength = 0;
    }

    public void getNewFrame(short[] newFrame) {
        audioWithPCM[audioLength] = newFrame;
        audioLength++;
    }

    public short[][] getAudio() {
        return audioWithPCM;
    }

    public short[] getNewPCMBuf() {
        return audioWithPCM[audioLength];
    }

    public int getLength() {
        return audioLength;
    }

    @Override
    public String toString() {
        for(int i=0; i<audioLength; i++) {
            System.out.println("第" + i +"帧音频：" + Arrays.toString(audioWithPCM[i]) + "  大小：" + audioWithPCM[i]);
        }
        return super.toString();
    }
}
