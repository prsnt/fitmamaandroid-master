package com.fitsoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fitsoo.model.ProgramExtras.ProgramExtraVideo;
import com.fitsoo.model.ProgramExtras.ProgramVideo;
import com.fitsoo.model.ProgramExtras.ProgramsExtraModel;
import com.fitsoo.model.program_profile.ProgramVido;

import java.util.List;

public class ProgramHomeModel implements Parcelable {

    private String demovideo_id;

    private String program_id;

    private String demoVideoThumb;

    private String DemoVideoUrl;

    private String program_title;

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    private String bundleId;

    public String getDownloadPDF() {
        return DownloadPDF;
    }

    public void setDownloadPDF(String downloadPDF) {
        DownloadPDF = downloadPDF;
    }

    private String DownloadPDF;

    private String description;
    private String focusName;
    private String no_of_workouts;
    private String no_of_days;
    private String minute;
    private String price;
    private Integer isProgramPurchased;
    private String Totalpercentage;
    private List<ProgramVideo> ProgramVidoes = null;

    public List<ProgramExtraVideo> getExtraVidoes() {
        return ExtraVidoes;
    }

    public void setExtraVidoes(List<ProgramExtraVideo> extraVidoes) {
        ExtraVidoes = extraVidoes;
    }

    private List<ProgramExtraVideo> ExtraVidoes = null;

    public String getTotalpercentage() {
        return Totalpercentage;
    }

    public void setTotalpercentage(String totalpercentage) {
        Totalpercentage = totalpercentage;
    }

    public List<ProgramVideo> getProgramInformation() {
        return ProgramVidoes;
    }

    public void setProgramInformation(List<ProgramVideo> programInformation) {
        this.ProgramVidoes = programInformation;
    }

    public Integer getIsAccess() {
        return isAccess;
    }

    public void setIsAccess(Integer isAccess) {
        this.isAccess = isAccess;
    }

    private Integer isAccess;
    private String unique_videos;

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

    public Integer getIsProgramPurchased() {
        return isProgramPurchased;
    }

    public void setIsProgramPurchased(Integer isProgramPurchased) {
        this.isProgramPurchased = isProgramPurchased;
    }

    public String getUniqueVideos() {
        return unique_videos;
    }

    public void setUniqueVideos(String uniqueVideos) {
        this.unique_videos = uniqueVideos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.demovideo_id);
        dest.writeString(this.program_id);
        dest.writeString(this.demoVideoThumb);
        dest.writeString(this.DemoVideoUrl);
        dest.writeString(this.program_title);
        dest.writeString(this.description);
        dest.writeString(this.focusName);
        dest.writeString(this.no_of_workouts);
        dest.writeString(this.no_of_days);
        dest.writeString(this.minute);
        dest.writeString(this.price);
        dest.writeValue(this.isProgramPurchased);
        dest.writeString(this.unique_videos);
    }

    public ProgramHomeModel() {
    }

    protected ProgramHomeModel(Parcel in) {
        this.demovideo_id = in.readString();
        this.program_id = in.readString();
        this.demoVideoThumb = in.readString();
        this.DemoVideoUrl = in.readString();
        this.program_title = in.readString();
        this.description = in.readString();
        this.focusName = in.readString();
        this.no_of_workouts = in.readString();
        this.no_of_days = in.readString();
        this.minute = in.readString();
        this.price = in.readString();
        this.isProgramPurchased = (Integer) in.readValue(Integer.class.getClassLoader());
        this.unique_videos = in.readString();
    }

    public static final Parcelable.Creator<ProgramHomeModel> CREATOR = new Parcelable.Creator<ProgramHomeModel>() {
        @Override
        public ProgramHomeModel createFromParcel(Parcel source) {
            return new ProgramHomeModel(source);
        }

        @Override
        public ProgramHomeModel[] newArray(int size) {
            return new ProgramHomeModel[size];
        }
    };
}
