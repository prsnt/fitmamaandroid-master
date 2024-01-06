package com.fitsoo.model.ProgramExtras;

import com.fitsoo.model.ProgramExtras.ProgramExtraVideo;
import com.fitsoo.model.ProgramExtras.ProgramVideo;

import java.util.List;

public class ProgramsExtraModel {

    private String success;

    private String message;

    public String getDownloadPDF() {
        return DownloadPDF;
    }

    public void setDownloadPDF(String downloadPDF) {
        DownloadPDF = downloadPDF;
    }

    private String DownloadPDF;

    public String getDownloadPDFName() {
        return DownloadPDFName;
    }

    public void setDownloadPDFName(String downloadPDFName) {
        DownloadPDFName = downloadPDFName;
    }

    private String DownloadPDFName;

    public float getProgramPercentage() {
        return ProgramPercentage;
    }

    public void setProgramPercentage(int programPercentage) {
        ProgramPercentage = programPercentage;
    }

    private float ProgramPercentage;

    private List<ProgramVideo> ProgramVideos = null;

    private List<ProgramExtraVideo> ProgramExtraVideos = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProgramVideo> getProgramVideos() {
        return ProgramVideos;
    }

    public void setProgramVideos(List<ProgramVideo> programVideos) {
        this.ProgramVideos = programVideos;
    }

    public List<ProgramExtraVideo> getProgramExtraVideos() {
        return ProgramExtraVideos;
    }

    public void setProgramExtraVideos(List<ProgramExtraVideo> programExtraVideos) {
        this.ProgramExtraVideos = programExtraVideos;
    }
}
