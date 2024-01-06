package com.fitsoo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fitsoo.R;
import com.fitsoo.activity.baseclass.BaseActivity;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.interfacepack.CustomDialogOneButtonListener;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.view.CustomTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity implements OnReceiveResponseListener, CustomDialogOneButtonListener {

    private EditText edtEmail;
    private Button btnResetPassword;
    private ImageView imgBack;
    private CustomTextView txtSignUp;
    private BaseResponse<String> baseResponse;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawableResource(R.mipmap.forgot_pass);
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Forgot password");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        setContentView(R.layout.activity_forgotpwd);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        txtSignUp = (CustomTextView) findViewById(R.id.txt_signup);

        imgBack = (ImageView) findViewById(R.id.signup_img_goback);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation()){
                    forgotPassword();
                }
            }
        });


        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passToSignUp = new Intent(ForgotPasswordActivity.this , SignupActivity.class);
                startActivity(passToSignUp);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passToSignUp = new Intent(ForgotPasswordActivity.this , LoginActivity.class);
                startActivity(passToSignUp);
                finish();
            }
        });
    }

    private boolean checkValidation() {

        if(!FitsooUtils.hasValue(edtEmail.getText().toString().trim())){
            FitsooUtils.showMessageAlert(getString(R.string.str_error_email_missing) , getString(R.string.app_name) , ForgotPasswordActivity.this);
            return false;
        } else if(!FitsooUtils.isValidEmail(edtEmail.getText().toString().trim())){
            FitsooUtils.showMessageAlert(getString(R.string.str_error_email_format) , getString(R.string.app_name) , ForgotPasswordActivity.this);
            return false;
        }

        return true;
    }


    private void forgotPassword() {
            JSONObject forgotRes = new JSONObject();
            try {
                forgotRes.put(FitsooConstant.REQ_EMAIL , edtEmail.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            FitsooUtils.performRequest(ForgotPasswordActivity.this , forgotRes.toString(),getString(R.string.BASE_URL) + getString(R.string.forgot_password), this,getString(R.string.str_please_wait), "forgotpassword");
    }

    @Override
    public void onResponseReceived(String requestType, String response) {
        switch (requestType){
            case "forgotpassword":
                    onReceiveRespnse(response);
                break;
        }
    }

    private void onReceiveRespnse(String res) {
       baseResponse = (new Gson()).fromJson(res , new TypeToken<BaseResponse<String>>(){}.getType());
        FitsooUtils.showMessageAlert(baseResponse.getMessage() , getString(R.string.app_name) , this , this);
    }

    @Override
    public void onDialogPositiveButtonClick(DialogInterface dialog, int which) {
        dialog.dismiss();
       onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (baseResponse!= null && baseResponse.getSuccess() == 1) {
            Intent passToLogin = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(passToLogin);
            finish();
        } else  if(baseResponse == null){
            Intent passToLogin = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(passToLogin);
            finish();
        }
    }
    @Override
    protected void onPause() {
        FitsooUtils.dismissProgressDialog(FitsooUtils.progressDialog);
        super.onPause();
    }
}
