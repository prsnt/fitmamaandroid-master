package com.fitsoo.model;

/**
 * Created by system  on 20/7/17.
 */

public class WorkoutModel {



    /**
     * instructions : - This workout includes 4 different - This workout includes 4 different - This workout includes 4 different
     * instructor_name : Myra Johnson
     * vid : 2
     * workfocus_name : Upper Body
     * worktype_name : Aerobics
     * videourl : http://testing.siliconithub.com/fitnesswebpanel/uploads/workout_videos/video.mp4
     * video_thumb : http://testing.siliconithub.com/fitnesswebpanel/uploads/workout_videos/thumb/videoplayback.jpeg
     * progress_time : 1500
     */

    private String instructions;
    private String instructor_name;
    private String vid;
    private String workfocus_name;
    private String worktype_name;
    private String videourl;
    private String video_thumb;
    private String progress_time;
    private String video_duration;

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getInstructor_name() {
        return instructor_name;
    }

    public void setInstructor_name(String instructor_name) {
        this.instructor_name = instructor_name;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getWorkfocus_name() {
        return workfocus_name;
    }

    public void setWorkfocus_name(String workfocus_name) {
        this.workfocus_name = workfocus_name;
    }

    public String getWorktype_name() {
        return worktype_name;
    }

    public void setWorktype_name(String worktype_name) {
        this.worktype_name = worktype_name;
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

    public String getProgress_time() {
        return progress_time;
    }

    public void setProgress_time(String progress_time) {
        this.progress_time = progress_time;
    }

    public String getVideo_duration() {
        return video_duration.trim();
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }
}
