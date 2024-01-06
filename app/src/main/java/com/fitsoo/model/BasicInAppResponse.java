package com.fitsoo.model;

/**
 * Created by system  on 7/9/17.
 */

public class BasicInAppResponse {


    /**
     * data : {"response_inapp":"{\"packageName\":\"com.fitsoo\",\"orderId\":\"transactionId.android.test.purchased\",\"productId\":\"android.test.purchased\",\"developerPayload\":\"\",\"purchaseTime\":0,\"purchaseState\":0,\"purchaseToken\":\"inapp:com.fitsoo:android.test.purchased\"}"}
     * success : 1
     * message : Success
     * acees_key : 0
     */

    private DataBean data;
    private String success;
    private String message;
    private String acees_key;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public String getAcees_key() {
        return acees_key;
    }

    public void setAcees_key(String acees_key) {
        this.acees_key = acees_key;
    }

    public static class DataBean {
        /**
         * response_inapp : {"packageName":"com.fitsoo","orderId":"transactionId.android.test.purchased","productId":"android.test.purchased","developerPayload":"","purchaseTime":0,"purchaseState":0,"purchaseToken":"inapp:com.fitsoo:android.test.purchased"}
         */

        private String response_inapp;
        private String device_type;

        public String getResponse_inapp() {
            return response_inapp;
        }

        public void setResponse_inapp(String response_inapp) {
            this.response_inapp = response_inapp;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }
    }
}
