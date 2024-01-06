package com.fitsoo.model.ProgramExtras;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgramVideo implements Parcelable {

    private String vid;
    private Integer program_id;
    private String VideoThumb;
    private String VideoUrl;
    private String program_title;
    private String workoutType;
    private String progress_time;
    private String progress_percentage;

    public String getProgress_time() {
        return progress_time;
    }

    public void setProgress_time(String progress_time) {
        this.progress_time = progress_time;
    }

    public String getProgress_percentage() {
        return progress_percentage;
    }

    public void setProgress_percentage(String progress_percentage) {
        this.progress_percentage = progress_percentage;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    private String video_duration;

    public String getVideoId() {
        return vid;
    }

    public void setVideoId(String videoId) {
        this.vid = videoId;
    }

    public Integer getProgramId() {
        return program_id;
    }

    public void setProgramId(Integer programId) {
        this.program_id = programId;
    }

    public String getVideoThumb() {
        return VideoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.VideoThumb = videoThumb;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.VideoUrl = videoUrl;
    }

    public String getProgramTitle() {
        return program_title;
    }

    public void setProgramTitle(String programTitle) {
        this.program_title = programTitle;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.vid);
        dest.writeValue(this.program_id);
        dest.writeString(this.VideoThumb);
        dest.writeString(this.VideoUrl);
        dest.writeString(this.program_title);
        dest.writeString(this.workoutType);
        dest.writeString(this.progress_time);
        dest.writeString(this.progress_percentage);
        dest.writeString(this.video_duration);
    }

    public ProgramVideo() {
    }

    protected ProgramVideo(Parcel in) {
        this.vid = in.readString();
        this.program_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.VideoThumb = in.readString();
        this.VideoUrl = in.readString();
        this.program_title = in.readString();
        this.workoutType = in.readString();
        this.progress_time = in.readString();
        this.progress_percentage = in.readString();
        this.video_duration = in.readString();
    }

    public static final Parcelable.Creator<ProgramVideo> CREATOR = new Parcelable.Creator<ProgramVideo>() {
        @Override
        public ProgramVideo createFromParcel(Parcel source) {
            return new ProgramVideo(source);
        }

        @Override
        public ProgramVideo[] newArray(int size) {
            return new ProgramVideo[size];
        }
    };
}