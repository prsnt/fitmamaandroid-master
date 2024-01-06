package com.fitsoo.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.fitsoo.constants.PrefConstant;

/**
 * Created by system  on 10/4/17.
 */

public class FitsooPref {

    // Instance of the Shared Preference where data will be stored
    private static SharedPreferences preference;

    // Will return the instance of current shared preference if there is any, else will create one
    private static SharedPreferences getInstance(Context context){
        if(preference == null){
            preference = context.getSharedPreferences("Fitsoo" , Context.MODE_PRIVATE);
        }
        return preference;
    }

    public static void clearSharePreference(Context context){
        getInstance(context).edit().clear().apply();
    }

    public static void setShouldShowSettingDialog(Context context , boolean showDialog){
        getInstance(context).edit().putBoolean(PrefConstant.SHOW_DIALOG_SETTING, showDialog).apply();
    }

    public static boolean getShouldShowDialog(Context context){
        return  getInstance(context).getBoolean(PrefConstant.SHOW_DIALOG_SETTING, true);
    }

    public static void setUserId(Context context , int userId){
        getInstance(context).edit().putInt(PrefConstant.USERID, userId).apply();
    }

    public static int getUserId(Context context){
        return  getInstance(context).getInt(PrefConstant.USERID, 0);
    }

    public static void setUserProfileInfo(Context context , String userProfileResponse){
        getInstance(context).edit().putString(PrefConstant.USER_PROFILE, userProfileResponse).apply();
    }

    public static String getUserProfileInfo(Context context){
        return  getInstance(context).getString(PrefConstant.USER_PROFILE, "");
    }

    public static void setSubscribeType(Context context , int setSubscribeType){
        getInstance(context).edit().putInt(PrefConstant.SUB_TYPE, setSubscribeType).apply();
    }

    public static int getSubscribeType(Context context){
        return  getInstance(context).getInt(PrefConstant.SUB_TYPE, 0);
    }
    public static void setValidTill(Context context , String setValidTill){
        getInstance(context).edit().putString(PrefConstant.VALID_TILL, setValidTill).apply();
    }

    public static String getValidTill(Context context){
        return  getInstance(context).getString(PrefConstant.VALID_TILL, "");
    }

    public static void setWorkoutRes(Context context, String strprof){
        getInstance(context).edit().putString(PrefConstant.WORKOUT_RESPONSE, strprof).apply();
    }

    public static String getWorkOutRes(Context context){
        return  getInstance(context).getString(PrefConstant.WORKOUT_RESPONSE, "");
    }

    // Set Challege Response data obtained
    public static void setChallengeRes(Context context, String strChallenge){
        getInstance(context).edit().putString(PrefConstant.CHALLENGE_RESPONSE, strChallenge).apply();
    }

    // Get Challenge Response data obtained
    public static String getChallengeRes(Context context){
        return  getInstance(context).getString(PrefConstant.CHALLENGE_RESPONSE, "");
    }

    // Set Challege Response data obtained
    public static void setPrograms(Context context, String strPrograms){
        getInstance(context).edit().putString(PrefConstant.PROGRAMS_RESPONSE, strPrograms).apply();
    }

    // Get Challenge Response data obtained
    public static String getPrograms(Context context){
        return  getInstance(context).getString(PrefConstant.PROGRAMS_RESPONSE, "");
    }

    // Get Challenge Response data obtained
    public static String getProgramsE(Context context){
        return  getInstance(context).getString(PrefConstant.PROGRAMSE_RESPONSE, "");
    }

    // Set Challege Response data obtained
    public static void setProgramsE(Context context, String strPrograms){
        getInstance(context).edit().putString(PrefConstant.PROGRAMSE_RESPONSE, strPrograms).apply();
    }

    public static void setString(Context context, String str){
        getInstance(context).edit().putString(PrefConstant.SUBSCRIPTION_RESP, str).apply();
    }

    public static int getAccess(Context context){
        return  getInstance(context).getInt(PrefConstant.ACCESS_RESP, 0);
    }

    public static void setAccess(Context context, int code){
        getInstance(context).edit().putInt(PrefConstant.ACCESS_RESP, code).apply();
    }

    public static String getAccessmsg(Context context){
        return  getInstance(context).getString(PrefConstant.ACCESS_MSG, "");
    }

    public static void setAccessmsg(Context context, String code){
        getInstance(context).edit().putString(PrefConstant.ACCESS_MSG, code).apply();
    }

    public static String getPurchaseToken(Context context){
        return  getInstance(context).getString(PrefConstant.PURCHASE_TOKEN, "");
    }

 public static String getpackage_bought_from(Context context){
        return  getInstance(context).getString(PrefConstant.PACKAGE_BOUGHT_FROM, "");
    }

    public static void setPurchaseToken(Context context, String code){
        getInstance(context).edit().putString(PrefConstant.PURCHASE_TOKEN, code).apply();
    }
    public static void setisNewUser(Context context, String code){
        getInstance(context).edit().putString(PrefConstant.ISNEWUSER, code).apply();
    }

    public static String getisNewUser (Context context){
        return  getInstance(context).getString(PrefConstant.ISNEWUSER, "");
    }

    public static void setpackage_bought_from(Context context, String code){
        getInstance(context).edit().putString(PrefConstant.PACKAGE_BOUGHT_FROM, code).apply();
    }

    public static String getString(Context context){
        return  getInstance(context).getString(PrefConstant.SUBSCRIPTION_RESP, "");
    }

    // Get Challenge Response data obtained
    public static String getProgramsExtra(Context context){
        return  getInstance(context).getString(PrefConstant.PROGRAMSE_RESPONSEXTRA, "");
    }

    // Set Challege Response data obtained
    public static void setProgramsExtra(Context context, String strPrograms){
        getInstance(context).edit().putString(PrefConstant.PROGRAMSE_RESPONSEXTRA, strPrograms).apply();
    }

    // Set Challege Response data obtained
    public static void setCMSresponse(Context context, String cmsRes){
        getInstance(context).edit().putString(PrefConstant.CMS_RESPONSE, cmsRes).apply();
    }

    // Get Challenge Response data obtained
    public static String getCMSResponse(Context context){
        return  getInstance(context).getString(PrefConstant.CMS_RESPONSE, "");
    }

    public static void saveLocation(double latitude , double longitude , Context context){
        getInstance(context).edit().putString(PrefConstant.LOCATION, latitude + "," +longitude).apply();
    }

    public static String[] getLatLong(Context context){
        String latlong  = getInstance(context).getString(PrefConstant.LOCATION, "0,0");
        String[] str = latlong.split(",");
        return str;
    }

//    public static void setWorkoutPosition(Context context , int position){
//        getInstance(context).edit().putInt(PrefConstant.WORKOUT_POSITION, position).apply();
//    }
//
//    public static Integer getWorkoutPosition(Context context){
//        return  getInstance(context).getInt(PrefConstant.WORKOUT_POSITION, 3);
//    }

}
