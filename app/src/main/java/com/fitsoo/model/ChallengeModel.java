package com.fitsoo.model;

/**
 * Created by system  on 21/7/17.
 */

public class ChallengeModel {


    /**
     * vid : 3
     * workfocus_name : Lower Body
     * worktype_name : Tabata
     * videourl : http://testing.siliconithub.com/fitnesswebpanel/uploads/workout_videos/video2.mp4
     * video_thumb : http://testing.siliconithub.com/fitnesswebpanel/uploads/workout_videos/thumb/training.jpg
     */

    /*{"success":"1","message":"Challenges video list.","data":[{"vid":"139","workfocus_name":"Core & Abs","worktype_name":"Six Pack","videourl":"https:\/\/dr7n943fq3qef.cloudfront.net\/151633893319332.mp4","video_thumb":"http:\/\/dashboard.fitsoo.com\/uploads\/workout_videos\/thumb\/1516339059six pack 5.jpg"},{"vid":"137","workfocus_name":"Lower Body","worktype_name":"Booty Boost","videourl":"https:\/\/dr7n943fq3qef.cloudfront.net\/151616639656538.mp4","video_thumb":"http:\/\/dashboard.fitsoo.com\/uploads\/workout_videos\/thumb\/1516166504Booty Boost 2.JPG"},{"vid":"136","workfocus_name":"Interval","worktype_name":"Tabata","videourl":"https:\/\/dr7n943fq3qef.cloudfront.net\/151616359120268.mp4","video_thumb":"http:\/\/dashboard.fitsoo.com\/uploads\/workout_videos\/thumb\/1516163644tabata 1.jpg"},{"vid":"121","workfocus_name":"Full Body","worktype_name":"MamaDance","videourl":"https:\/\/dr7n943fq3qef.cloudfront.net\/151399076779648.mp4","video_thumb":"http:\/\/dashboard.fitsoo.com\/uploads\/workout_videos\/thumb\/1514047514Mamadance 2.JPG"},{"vid":"95","workfocus_name":"Interval","worktype_name":"Speedy","videourl":"https:\/\/dr7n943fq3qef.cloudfront.net\/151276054945517.mp4","video_thumb":"http:\/\/dashboard.fitsoo.com\/uploads\/workout_videos\/thumb\/1512791654Speedy 3.JPG"},{"vid":"71","workfocus_name":"Full Body","worktype_name":"Bottle Fit","videourl":"https:\/\/dr7n943fq3qef.cloudfront.net\/151016747558274.mp4","video_thumb":"http:\/\/dashboard.fitsoo.com\/uploads\/workout_videos\/thumb\/1510169374Bottle Fit 2.JPG"}]}*/

    private String vid;
    private String workfocus_name;
    private String worktype_name;
    private String videourl;
    private String video_thumb;

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
}
