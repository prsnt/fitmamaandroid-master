package com.fitsoo.model.program_profile;

public class ProgramVido {

    private String program_id;
    private String progress_type;
    private String type;
    private String progress;
    private String vid;
    private String videourl;
    private String video_thumb;
    private String workoutdate;
    private String video_duration;
    private String percentage;

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }


    public String getProgramId() {
        return program_id;
    }

    public void setProgramId(String programId) {
        this.program_id = programId;
    }

    public String getProgressType() {
        return progress_type;
    }

    public void setProgressType(String progressType) {
        this.progress_type = progressType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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


    public String getWorkoutdate() {
        return workoutdate;
    }

    public void setWorkoutdate(String workoutdate) {
        this.workoutdate = workoutdate;
    }


    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

}