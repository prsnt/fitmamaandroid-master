package com.fitsoo.model;

import java.util.List;

/**
 * Created by system  on 28/7/17.
 */

public class BaseResponse<T> {
    private int success;
    private String message;
    private T data;
    private List<Extravideo> extravideos;

    public List<Extravideo> getExtravideos() {
        return extravideos;
    }

    public void setExtravideos(List<Extravideo> extravideos) {
        this.extravideos = extravideos;
    }

    public List<ProgramHomeModel> getProgramvideos() {
        return programVideos;
    }

    public void setProgramvideos(List<ProgramHomeModel> programvideos) {
        this.programVideos = programvideos;
    }

    private List<ProgramHomeModel> programVideos;

    private String service_call_time;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getService_call_time() {
        return service_call_time;
    }

    public void setService_call_time(String service_call_time) {
        this.service_call_time = service_call_time;
    }
}
