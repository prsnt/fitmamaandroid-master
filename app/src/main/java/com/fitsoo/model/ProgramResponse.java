package com.fitsoo.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ProgramResponse
{

    private int success;

    private String message;

    private List<ProgramHomeModel> programInformation = null;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProgramHomeModel> getProgramInformation() {
        return programInformation;
    }

    public void setProgramInformation(List<ProgramHomeModel> programInformation) {
        this.programInformation = programInformation;
    }

    public class ProgramInformation {

        private String demovideo_id;

        private String program_id;

        private String demoVideoThumb;

        private String DemoVideoUrl;

        private String program_title;

        private String description;

        private String focusName;

        private String no_of_workouts;

        private String no_of_days;

        private String minute;

        private String price;

        private String unique_videos;

        private Integer isProgramPurchased;

        public Integer getIsProgramPurchased() {
            return isProgramPurchased;
        }

        public void setIsProgramPurchased(Integer isProgramPurchased) {
            this.isProgramPurchased = isProgramPurchased;
        }

        public String getDemovideoId() {
            return demovideo_id;
        }

        public void setDemovideoId(String demovideoId) {
            this.demovideo_id = demovideoId;
        }

        public String getProgramId() {
            return program_id;
        }

        public void setProgramId(String programId) {
            this.program_id = programId;
        }

        public String getDemoVideoThumb() {
            return demoVideoThumb;
        }

        public void setDemoVideoThumb(String demoVideoThumb) {
            this.demoVideoThumb = demoVideoThumb;
        }

        public String getDemoVideoUrl() {
            return DemoVideoUrl;
        }

        public void setDemoVideoUrl(String demoVideoUrl) {
            this.DemoVideoUrl = demoVideoUrl;
        }

        public String getProgramTitle() {
            return program_title;
        }

        public void setProgramTitle(String programTitle) {
            this.program_title = programTitle;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFocusName() {
            return focusName;
        }

        public void setFocusName(String focusName) {
            this.focusName = focusName;
        }

        public String getNoOfWorkouts() {
            return no_of_workouts;
        }

        public void setNoOfWorkouts(String noOfWorkouts) {
            this.no_of_workouts = noOfWorkouts;
        }

        public String getNoOfDays() {
            return no_of_days;
        }

        public void setNoOfDays(String noOfDays) {
            this.no_of_days = noOfDays;
        }

        public String getMinute() {
            return minute;
        }

        public void setMinute(String minute) {
            this.minute = minute;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUniqueVideos() {
            return unique_videos;
        }

        public void setUniqueVideos(String uniqueVideos) {
            this.unique_videos = uniqueVideos;
        }

    }
}
