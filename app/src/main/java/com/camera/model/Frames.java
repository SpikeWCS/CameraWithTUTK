package com.camera.model;

public class Frames {
    private byte[] frame;
    public boolean IFrame;
    private int size;

    public byte[] getFrame() {
        return frame;
    }

    public void setFrame(byte[] bytes) {
        frame = bytes;
    }

    public void setIFrame(boolean IFrame) {
        this.IFrame = IFrame;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
