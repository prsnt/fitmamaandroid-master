package com.fitsoo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fitsoo.R;
import com.fitsoo.activity.baseclass.BaseActivity;
import com.fitsoo.interfacepack.CustomDialogTwoButtonListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.ProfileReponseModel;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.view.CustomButton;
import com.fitsoo.view.CustomTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * Created by system  on 18/7/17.
 */

public class WelcomeActivity extends BaseActivity {

    private CustomButton btnLetsStart;
    private CustomTextView txtUserName;
    private CircularImageView imgProfile;
    private ImageView imgBack;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static boolean IsfromLoginOrSignup = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Welcome");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        setContentView(R.layout.activity_welcome_screen);

        imgBack = (ImageView) findViewById(R.id.signup_img_goback);
        btnLetsStart = (CustomButton) findViewById(R.id.btn_lets_get_started);
        imgProfile = (CircularImageView) findViewById(R.id.img_profile);
        txtUserName = (CustomTextView) findViewById(R.id.txt_welcome_name);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        BaseResponse<ProfileReponseModel> profResponse = (new Gson()).fromJson(FitsooPref.getUserProfileInfo(WelcomeActivity.this), new TypeToken<BaseResponse<ProfileReponseModel>>() {
        }.getType());
        txtUserName.setText(profResponse.getData().getFirst_name() + " " + profResponse.getData().getLast_name());
        Glide.with(WelcomeActivity.this).load(profResponse.getData().getProfile_pic()).into(imgProfile);

        btnLetsStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passToMain;
                if (FitsooPref.getSubscribeType(WelcomeActivity.this) == -1 || !(FitsooPref.getSubscribeType(WelcomeActivity.this) == 1 && FitsooPref.getSubscribeType(WelcomeActivity.this) == 2)) {
//                    LoginActivity.isfromLogin = true;
//                    passToMain = new Intent(WelcomeActivity.this, SubscribeActivity.class);
//                    startActivity(passToMain);
                    passToMain = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(passToMain);
                    finish();
                } else {
                    passToMain = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(passToMain);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        FitsooUtils.showMessageAlert("Are you sure you want to exit?", getString(R.string.app_name), WelcomeActivity.this, new CustomDialogTwoButtonListener() {
            @Override
            public void onDialogPositiveButtonClick(DialogInterface dialog, int which) {
                WelcomeActivity.super.onBackPressed();
            }

            @Override
            public void onDialogNegativeButtonClick(DialogInterface dialog, int which) {

            }
        }, "Ok", "Cancel");
    }

    @Override
    protected void onResume() {
        super.onResume();
        IsfromLoginOrSignup = true;
    }

    @Override
    protected void onPause() {
        FitsooUtils.dismissProgressDialog(FitsooUtils.progressDialog);
        super.onPause();
    }
}
