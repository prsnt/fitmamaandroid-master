package com.fitsoo.constants;

import java.util.regex.Pattern;

/**
 * Created by xyz on 22/7/17.
 */

public class FitsooConstant {

    public static final int PASSWORD_LENGTH = 6;
    public static final String PROFILE_IMAGE_NAME = "fitsoo_profile_image";
//    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,15}";
    public static final String PASSWORD_PATTERN = "[\\D\\d.]{6,16}";

    //-----------------------------Request Param List------------------------------------//

    public static final String REQ_USER_ID = "user_id";
    public static final String REQ_OLD_PASSWORD = "old_password";
    public static final String REQ_NEW_PASSWORD = "new_password";
    public static final String REQ_CONFIRM_NEW_PASSWORD = "confirm_password";
    public static final String REQ_EMAIL = "email";
    public static final String REQ_PASSWORD = "password";
    public static final String REQ_GOOGLE_PLUS_ID = "gp_id";
    public static final String REQ_LATITUDE = "latitude";
    public static final String REQ_LONGITUDE = "longitude";
    public static final String REQ_ID = "id";
    public static final String PROGRAM_ID = "program_id";
    public static final String REQ_TIMEZONE = "timezone";
    public static final String REQ_FACEBOOK_ID = "fb_id";
    public static final String REQ_DEVICE_TOKEN = "device_token";
    public static final String REQ_DEVICE_TYPE = "device_type";


    //-----------------------------SERVICE PARAMETERS--------------------------------------//
    // For Header Password
    public static final String REQ_HEADER_PASSWORD = "password";

    // For Header Username
    public static final String REQ_HEADER_USERNAME = "username";

    // For Header Token
    public static final String REQ_HEADER_TOKEN = "token";

    // For Header Content Type
    public static final String REQ_HEADER_CONTENT_TYPE = "Content-Type";

}
