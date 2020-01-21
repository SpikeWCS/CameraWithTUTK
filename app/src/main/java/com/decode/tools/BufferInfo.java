package com.decode.tools;

/**
 * videobuffer信息类
 */

public class BufferInfo {
    public int len;// videobuffer的有效长度
    public byte[] buffer;// videobuffer

    public BufferInfo(int len,byte[] buffer){
        this.len=len;
        this.buffer=buffer;
    }
}
