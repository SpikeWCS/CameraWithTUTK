package com.camera.model;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;

import com.decode.tools.AvcUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Muxer {
    //  Throw IOException
    public MediaMuxer mediaMuxer = null;

    public MediaFormat initVideoTrackFormat() {
        MediaFormat videoTrackFormat;
        byte[] sps = {0, 0, 0, 1, 103, 77, 0, 30, -107, -88, 40, 11, -2, 89, -72, 8, 8, 8, 16};
        byte[] sps_hd = {0, 0, 0, 1, 103, 77, 0, 31, -107, -88, 20, 1, 110, -101, -128, -128, -128, -127};
        byte[] pps = {0, 0, 0, 1, 104, -18, 60, -128};
        int[] width = new int[1];
        int[] height = new int[1];
        AvcUtils.parseSPS(sps, width, height);
        videoTrackFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width[0], height[0]);
        videoTrackFormat.setByteBuffer("csd-0", ByteBuffer.wrap(sps));
        videoTrackFormat.setByteBuffer("csd-1", ByteBuffer.wrap(pps));
        videoTrackFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        videoTrackFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, width[0] * height[0]);
        videoTrackFormat.setInteger(MediaFormat.KEY_BIT_RATE, width[0] * height[0]);
        videoTrackFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);

        return videoTrackFormat;
    }


    private final String dirpath =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/TUTK_VIDEOS";
    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
    private final String path = dirpath + "/" + df.format(new Date()) + ".mp4";

    public void muxer(ArrayList<Frames> videoBytes) {
        System.out.println("VideoBytes Size: " + videoBytes.size());
        File dir = new File(dirpath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File video = new File(path);
        if (video.exists()) {
            video.delete();
        }
        try {
//            初始化MediaMuxer
            mediaMuxer = new MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//            初始化音频视频轨道
            int videoTrackIndex = mediaMuxer.addTrack(initVideoTrackFormat());
//            帧率
            int frameRate = initVideoTrackFormat().getInteger(MediaFormat.KEY_FRAME_RATE);

            MediaCodec.BufferInfo videoTrackInfo = new MediaCodec.BufferInfo();
            videoTrackInfo.presentationTimeUs = 0;

            mediaMuxer.start();


//            写入Buffer Data
            for (int i = 0; i < videoBytes.size(); i++) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(videoBytes.get(i).getFrame(), 0, videoBytes.get(i).getSize());
                videoTrackInfo.presentationTimeUs = i * 1000000 / frameRate;
                videoTrackInfo.offset = 0;
                videoTrackInfo.size = videoBytes.get(i).getSize();
                videoTrackInfo.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME;
                if (videoBytes.get(i).IFrame) {
                    videoTrackInfo.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME;
//                    System.out.println("I " + videoTrackInfo.size);
                } else {
                    videoTrackInfo.flags = MediaCodec.BUFFER_FLAG_PARTIAL_FRAME;
//                    System.out.println("P " + videoTrackInfo.size);
                }
                mediaMuxer.writeSampleData(videoTrackIndex, byteBuffer, videoTrackInfo);
//                System.out.println("Write Video Frame " + i);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            关闭MediaMuxer
            if (mediaMuxer != null) {
//                生成.MP4
                mediaMuxer.stop();
                mediaMuxer.release();
            }
        }
    }


}
