package com.camera.camerawithtutk;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.util.Log;

import com.camera.api.G711Code;
import com.camera.model.Audio;
import com.tutk.IOTC.AVAPIs;

import java.io.DataInputStream;
import java.util.Arrays;

// 获取音频的线程
public class AudioThread implements Runnable {
    private AudioTrack mAudioTrack;
    private DataInputStream mDis;//播放文件的数据流
    private Thread mRecordThread;
    private boolean isStart = false;
    private volatile static AudioThread mInstance;
    //音频流类型
    private static final int mStreamType = AudioManager.STREAM_MUSIC;
    //指定采样率 （MediaRecoder 的采样率通常是8000Hz AAC的通常是44100Hz。 设置采样率为44100，目前为常用的采样率，官方文档表示这个值可以兼容所有的设置）
    private static final int mSampleRateInHz = 8000;
    //指定捕获音频的声道数目。在AudioFormat类中指定用于此的常量
    private static final int mChannelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO; //单声道
    //指定音频量化位数 ,在AudioFormaat类中指定了以下各种可能的常量。通常我们选择ENCODING_PCM_16BIT和ENCODING_PCM_8BIT PCM代表的是脉冲编码调制，它实际上是原始音频样本。
    //因此可以设置每个样本的分辨率为16位或者8位，16位将占用更多的空间和处理能力,表示的音频也更加接近真实。
    private static final int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
    //指定缓冲区大小。调用AudioRecord类的getMinBufferSize方法可以获得。
    private int mMinBufferSize;
    //STREAM的意思是由用户在应用程序通过write方式把数据一次一次得写到audiotrack中。这个和我们在socket中发送数据一样，
    // 应用层从某个地方获取数据，例如通过编解码得到PCM数据，然后write到audiotrack。
    private static int mMode = AudioTrack.MODE_STREAM;


    static final int AUDIO_BUF_SIZE = 1024;
    static final int FRAME_INFO_SIZE = 16;

    private int avIndex;

    public AudioThread(int avIndex) {
        this.avIndex = avIndex;
        initData();
    }

    private void initData() {
        //根据采样率，采样精度，单双声道来得到frame的大小。
        mMinBufferSize = AudioTrack.getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);//计算最小缓冲区
        //注意，按照数字音频的知识，这个算出来的是一秒钟buffer的大小。
        //创建AudioTrack
        mAudioTrack = new AudioTrack(mStreamType, mSampleRateInHz, mChannelConfig,
                mAudioFormat, mMinBufferSize, mMode);
    }

    @Override
    public void run() {
        System.out.printf("[%s] 开始获取音频\n",
                Thread.currentThread().getName());

        AVAPIs av = new AVAPIs();
        byte[] frameInfo = new byte[FRAME_INFO_SIZE];
        byte[] audioBuffer = new byte[AUDIO_BUF_SIZE];

        Audio audio = new Audio();

        while (true) {
            // 响应中断
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Java技术栈线程被中断，程序退出。");
                return;
            }

            // ret用来判断是不是应该偶从缓冲区获取音频了，这样能保证音频质量
            int ret = av.avCheckAudioBuf(avIndex);

            if (ret < 0) {
                // Same error codes as below
                System.out.printf("[%s] avCheckAudioBuf() failed: %d\n",
                        Thread.currentThread().getName(), ret);
                break;
            } else if (ret < 3) {
                // 此时还不该获取，直接 sleep
                try {
                    Thread.sleep(120);
                    continue;
                } catch (InterruptedException e) {
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
            } else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {
                System.out.printf("[%s] AV_ER_REMOTE_TIMEOUT_DISCONNECT\n",
                        Thread.currentThread().getName());
                break;
            } else if (ret == AVAPIs.AV_ER_INVALID_SID) {
                System.out.printf("[%s] Session cant be used anymore\n",
                        Thread.currentThread().getName());
                break;
            } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                //System.out.printf("[%s] Audio frame losed\n",
                //        Thread.currentThread().getName());
                continue;
            }
            Log.d("ret", String.valueOf(ret));
            // Now the data is ready in audioBuffer[0 ... ret - 1]
            // Do something here
            //audio.getNewFrame(AudioThread.convertG711ToPCM(audioBuffer, ret, audio.getNewPCMBuf()));
            if (ret > 0) {
                audio.getNewFrame(G711Code.G711aDecoder(new short[ret], audioBuffer, ret));
                Log.d("audio1", Arrays.toString(G711Code.G711aDecoder(new short[ret], audioBuffer, ret)));
                if (audio.getLength() == 10) {
                    System.out.println(audio);
                }
                if (mAudioTrack.getState() == AudioTrack.STATE_UNINITIALIZED) {
                    initData();
                }
                mAudioTrack.play();
                mAudioTrack.write(G711Code.G711aDecoder(new short[ret], audioBuffer, ret), 0, ret);
            }
        }
        if (mAudioTrack != null) {
            if (mAudioTrack.getState() == AudioRecord.STATE_INITIALIZED) {//初始化成功
                mAudioTrack.stop();//停止播放
            }
            if (mAudioTrack != null) {
                mAudioTrack.release();//释放audioTrack资源
            }
        }
        System.out.printf("[%s] 音频线程退出\n",
                Thread.currentThread().getName());
    }
}

