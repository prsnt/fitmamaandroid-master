package com.fitsoo.model;

/**
 * Created by system  on 4/8/17.
 */

public class TermsAndPrivacy {


    /**
     * ios : http://fitsoo.siliconithub.us/
     * android : http://fitsoo.siliconithub.us/
     * privacy : http://dashboard.fitsoo.com/cmspage/privacy-policy.php
     * terms : http://dashboard.fitsoo.com/cmspage/terms-conditions.php
     */

    private String ios;
    private String android;
    private String privacy;
    private String terms;

    public String getIos() {
        return ios;
    }

    public void setIos(String ios) {
        this.ios = ios;
    }

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
}
