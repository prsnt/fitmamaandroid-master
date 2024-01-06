package com.fitsoo.model;

/**
 * Created by system  on 28/7/17.
 */

public class ProfileReponseModel {


    /**
     * id : 15
     * first_name : ashish
     * last_name : khatod
     * email : ashish@siliconithub.com
     * dob : 2017-06-11
     * profile_pic : http://testing.siliconithub.com/fitsoo/uploads/profile_pictures/original/1501066294205726.png
     * notification_status : 1
     */

    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String dob;
    private String profile_pic;
    private String notification_status;
    private String valid_till;

    public String getValid_till() {
        return valid_till;
    }

    public void setValid_till(String valid_till) {
        this.valid_till = valid_till;
    }

    public int getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(int subscription_type) {
        this.subscription_type = subscription_type;
    }

    private int subscription_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public void setNotification_status(String notification_status) {
        this.notification_status = notification_status;
    }
}
