package com.fitsoo.utils;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;

public class MyApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        MultiDex.install(this);
    }

}
