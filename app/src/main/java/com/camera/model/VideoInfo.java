package com.camera.model;

import java.io.File;

public class VideoInfo {
    private String videoName;
    private File videoPath;

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setVideoPath(File videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoName() {
        return videoName;
    }

    public File getVideoPath() {
        return videoPath;
    }

}
