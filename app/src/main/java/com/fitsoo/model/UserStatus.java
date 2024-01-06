package com.fitsoo.model;

/**
 * Created by system  on 11/9/17.
 */

public class UserStatus {


    /**
     * isLocked : 1
     * isDelete : 0
     */

    private String isLocked;
    private String isDelete;

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
