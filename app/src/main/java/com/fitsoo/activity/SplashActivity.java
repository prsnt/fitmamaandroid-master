package com.fitsoo.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;


import com.fitsoo.R;
import com.fitsoo.activity.baseclass.BaseActivity;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class SplashActivity extends BaseActivity {
    Handler handler;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Splash Screen");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        init();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        Intent intent = new Intent();
        intent.setAction("com.timer.broadcast_receiver");
        sendBroadcast(intent);
    }

    private void init() {

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!FitsooUtils.hasValue(FitsooPref.getUserProfileInfo(SplashActivity.this))) {
                    passToLoginScreen();
                } else {
                    passToMainActivity();
                }

            }
        }, 2000);
    }

    private void passToMainActivity() {
        Intent loginActivity = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(loginActivity);
        finish();
    }

    private void passToLoginScreen() {
        Intent loginActivity = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }
}
