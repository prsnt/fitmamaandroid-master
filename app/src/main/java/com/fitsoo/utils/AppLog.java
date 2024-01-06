package com.fitsoo.utils;

import android.util.Log;

    //DEVELOPER NAME : System

    //PROJECT NAME : Fitsoo

    //PURPOSE : THIS CLASS IS USED TO PRINT LOG IN WHOLE APPLICATION AND ALSO YOU CAN LOG OFF WHEN YOU PUBLISH APP USING JUST CHANGE STATUS WITH FALSE INSTEAD OF TRUE.

public class AppLog {

    private static boolean sFlagDebug = true;
    private static boolean sFlagInfo = true;
    private static boolean sFlagErr = true;

    // PURPOSE : TO PRINT DEBUG LOG.
    public static void d(String tag, String message) {
        if (sFlagDebug) {
            Log.d(tag, message);
        }
    }

    // PURPOSE : TO PRINT INFO LOG.
    public static void i(String tag, String message) {
        if (sFlagInfo) {
            Log.i(tag, message);
        }
    }

    // PURPOSE : TO PRINT ERROR LOG.
    public static void e(String tag, String message) {
        if (sFlagErr) {
            Log.e(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (sFlagErr) {
            Log.w(tag, message);
        }
    }
}
