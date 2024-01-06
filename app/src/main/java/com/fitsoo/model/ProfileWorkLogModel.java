package com.fitsoo.model;

/**
 * Created by system  on 21/7/17.
 */

public class ProfileWorkLogModel {

    /**
     * name : Tabata
     * progress : 9800
     * vid : 3
     * videourl : http://testing.siliconithub.com/fitnesswebpanel/uploads/workout_videos/video2.mp4
     * video_thumb : http://testing.siliconithub.com/fitnesswebpanel/uploads/workout_videos/thumb/training.jpg
     * workoutdate : 08-10-2017
     */

    private String name;
    private String progress;
    private String vid;
    private String videourl;
    private String video_thumb;
    private String workoutdate;
    private String video_duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    public String getWorkoutdate() {
        return workoutdate;
    }

    public void setWorkoutdate(String workoutdate) {
        this.workoutdate = workoutdate;
    }

    public String getVideo_duration() {
        return video_duration.trim();
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }
}
