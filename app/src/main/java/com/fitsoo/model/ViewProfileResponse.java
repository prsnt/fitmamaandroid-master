package com.fitsoo.model;

import com.fitsoo.model.program_profile.ProgramInformation;

import java.util.List;

/**
 * Created by system  on 11/8/17.
 */

public class ViewProfileResponse {
    /**
     * first_name : Vaibhav
     * last_name :      Itialian
     * email : vaibhav.italy1@gmail.com
     * profile_pic : http://testing.siliconithub.com/fitnesswebpanel/uploads/profile_pictures/original/1502347101466620.jpg
     * dob : 08-04-2017
     * notification_status : 0
     * workout_log : [{"name":"Tabata","progress":"9800","vid":"3","videourl":"http://testing.siliconithub.com/fitnesswebpanel/uploads/workout_videos/video2.mp4","video_thumb":"http://testing.siliconithub.com/fitnesswebpanel/uploads/workout_videos/thumb/training.jpg","workoutdate":"08-10-2017"},{"name":"Aerobics","progress":"2720","vid":"2","videourl":"http://testing.siliconithub.com/fitnesswebpanel/uploads/workout_videos/video.mp4","video_thumb":"http://testing.siliconithub.com/fitnesswebpanel/uploads/workout_videos/thumb/videoplayback.jpeg","workoutdate":"08-10-2017"}]
     */

    private String first_name;
    private String last_name;
    private String email;
    private String profile_pic;
    private String dob;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    private String language;
    private String notification_status;
    private List<ProfileWorkLogModel> workout_log;

    public List<ProgramHomeModel> getProgramInformation() {
        return programInformation;
    }

    public void setProgramInformation(List<ProgramHomeModel> programInformation) {
        this.programInformation = programInformation;
    }

    private List<ProgramHomeModel> programInformation;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public void setNotification_status(String notification_status) {
        this.notification_status = notification_status;
    }

    public List<ProfileWorkLogModel> getWorkout_log() {
        return workout_log;
    }

    public void setWorkout_log(List<ProfileWorkLogModel> workout_log) {
        this.workout_log = workout_log;
    }

}
