package com.fitsoo.model;

/**
 * Created by system  on 11/9/17.
 */

public class InAppResponseModel {


    /**
     * packageName : com.siliconinapptest.testproject
     * orderId : transactionId.android.test.purchased
     * productId : android.test.purchased
     * developerPayload :
     * purchaseTime : 0
     * purchaseState : 0
     * purchaseToken : inapp:com.siliconinapptest.testproject:android.test.purchased
     */

    private String packageName;
    private String orderId;
    private String productId;
    private String developerPayload;
    private long purchaseTime;
    private int purchaseState;
    private String purchaseToken;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDeveloperPayload() {
        return developerPayload;
    }

    public void setDeveloperPayload(String developerPayload) {
        this.developerPayload = developerPayload;
    }

    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public int getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(int purchaseState) {
        this.purchaseState = purchaseState;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }
}
