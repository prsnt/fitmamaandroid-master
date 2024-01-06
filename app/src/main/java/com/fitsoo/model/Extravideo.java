package com.fitsoo.model;

public class Extravideo {

    private String title;
    private String instructions;
    private String instructor_name;
    private String workfocus_name;
    private String worktype_name;
    private String video_thumb;
    private String progress_time;

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    private String videourl;
    private String vid;
    private String video_duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getInstructorName() {
        return instructor_name;
    }

    public void setInstructorName(String instructorName) {
        this.instructor_name = instructorName;
    }

    public String getWorkfocusName() {
        return workfocus_name;
    }

    public void setWorkfocusName(String workfocusName) {
        this.workfocus_name = workfocusName;
    }

    public String getWorktypeName() {
        return worktype_name;
    }

    public void setWorktypeName(String worktypeName) {
        this.worktype_name = worktypeName;
    }

    public String getVideoThumb() {
        return video_thumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.video_thumb = videoThumb;
    }

    public String getProgressTime() {
        return progress_time;
    }

    public void setProgressTime(String progressTime) {
        this.progress_time = progressTime;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVideoDuration() {
        return video_duration;
    }

    public void setVideoDuration(String videoDuration) {
        this.video_duration = videoDuration;
    }
}