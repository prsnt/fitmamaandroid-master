package com.fitsoo.model.ProgramExtras;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgramExtraVideo implements Parcelable {


    private String vid;

    private Integer program_id;
    private String VideoThumb;
    private String VideoUrl;
    private String program_title;
    private String workoutType;
    private String video_duration;

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }


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
        dest.writeString(this.video_duration);
    }

    public ProgramExtraVideo() {
    }

    protected ProgramExtraVideo(Parcel in) {
        this.vid = in.readString();
        this.program_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.VideoThumb = in.readString();
        this.VideoUrl = in.readString();
        this.program_title = in.readString();
        this.workoutType = in.readString();
        this.video_duration = in.readString();
    }

    public static final Parcelable.Creator<ProgramExtraVideo> CREATOR = new Parcelable.Creator<ProgramExtraVideo>() {
        @Override
        public ProgramExtraVideo createFromParcel(Parcel source) {
            return new ProgramExtraVideo(source);
        }

        @Override
        public ProgramExtraVideo[] newArray(int size) {
            return new ProgramExtraVideo[size];
        }
    };
}