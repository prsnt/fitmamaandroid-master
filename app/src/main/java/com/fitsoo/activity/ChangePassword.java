package com.fitsoo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.fitsoo.activity.baseclass.BaseActivity;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.interfacepack.CustomDialogOneButtonListener;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.ProfileReponseModel;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.view.CustomButton;
import com.fitsoo.view.CustomEdittext;
import com.fitsoo.R;
import com.fitsoo.preference.FitsooPref;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by system  on 19/7/17.
 */

public class ChangePassword extends BaseActivity implements OnReceiveResponseListener, CustomDialogOneButtonListener {

    private CustomEdittext email;
    private CustomEdittext oldPassword;
    private CustomEdittext newPassword;
    private CustomEdittext confirmNewPassword;
    private CustomButton btnChangePassword;
    private ImageView imgBack;
    private boolean isPasswordChanged;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setBackgroundDrawableResource(R.mipmap.signup);
        FitsooUtils.hideSoftKeyboard(ChangePassword.this);
        super.onCreate(savedInstanceState);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Change password");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.fragment_changepassword);
        imgBack = (ImageView) findViewById(R.id.signup_img_goback);
        email = (CustomEdittext) findViewById(R.id.edt_email);
        oldPassword = (CustomEdittext) findViewById(R.id.edt_old_password);
        newPassword = (CustomEdittext) findViewById(R.id.edt_new_password);
        confirmNewPassword = (CustomEdittext) findViewById(R.id.edt_confirm_new_password);

        btnChangePassword = (CustomButton) findViewById(R.id.btn_change_password);


        BaseResponse<ProfileReponseModel> model = (new Gson()).fromJson(FitsooPref.getUserProfileInfo(ChangePassword.this), new TypeToken<BaseResponse<ProfileReponseModel>>() {
        }.getType());
        email.setText(model.getData().getEmail());
        email.setEnabled(false);
        oldPassword.requestFocus();

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkChangePasswordValidation()) {
                    doChangePassword();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent passToSignUp = new Intent(ChangePassword.this , MainActivity.class);
                startActivity(passToSignUp);
                finish();*/
                onBackPressed();
            }
        });

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(oldPassword,
                InputMethodManager.SHOW_IMPLICIT);

        FitsooUtils.checkUserStatus(ChangePassword.this);
    }


    private boolean checkChangePasswordValidation() {
        if (!FitsooUtils.hasValue(oldPassword.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_old_password_error), getString(R.string.app_name), ChangePassword.this);
            return false;
        } else if (!FitsooUtils.hasValue(newPassword.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_new_password_error), getString(R.string.app_name), ChangePassword.this);
            return false;
        } else if (!FitsooUtils.isOfMinimumLength(newPassword.getText().toString().trim(), FitsooConstant.PASSWORD_LENGTH)
                || !FitsooUtils.isExpressionCorrect(newPassword.getText().toString().trim(), FitsooConstant.PASSWORD_PATTERN)) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_password_length), getString(R.string.app_name), ChangePassword.this);
            return false;
        } else if (!FitsooUtils.hasValue(confirmNewPassword.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_confirm_password_error), getString(R.string.app_name), ChangePassword.this);
            return false;
        } else if (!confirmNewPassword.getText().toString().trim().equals(newPassword.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_password_and_confirm_password), getString(R.string.app_name), ChangePassword.this);
            return false;
        }

        return true;
    }


    private void doChangePassword() {
        JSONObject chgPwdReq = new JSONObject();
        try {
            chgPwdReq.put(FitsooConstant.REQ_USER_ID, FitsooPref.getUserId(ChangePassword.this));
            chgPwdReq.put(FitsooConstant.REQ_OLD_PASSWORD, oldPassword.getText().toString().trim());
            chgPwdReq.put(FitsooConstant.REQ_NEW_PASSWORD, newPassword.getText().toString().trim());
            chgPwdReq.put(FitsooConstant.REQ_CONFIRM_NEW_PASSWORD, confirmNewPassword.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FitsooUtils.performRequest(ChangePassword.this, chgPwdReq.toString(), getString(R.string.BASE_URL) + getString(R.string.changepassword), this, getString(R.string.str_please_wait), "changepassword");
    }

    @Override
    public void onResponseReceived(String requestType, String response) {
        switch (requestType) {
            case "changepassword":
                BaseResponse changePass = (new Gson()).fromJson(response, new TypeToken<BaseResponse>() {
                }.getType());
                isPasswordChanged = changePass.getSuccess() == 1 ? true : false;
                FitsooUtils.showMessageAlert(changePass.getMessage(), getString(R.string.app_name), this, this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent passToMain = new Intent(ChangePassword.this , MainActivity.class);
        startActivity(passToMain);
        finish();
    }

    @Override
    public void onDialogPositiveButtonClick(DialogInterface dialog, int which) {
        if (isPasswordChanged) {
            onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        FitsooUtils.dismissProgressDialog(FitsooUtils.progressDialog);
        super.onPause();
    }
}
