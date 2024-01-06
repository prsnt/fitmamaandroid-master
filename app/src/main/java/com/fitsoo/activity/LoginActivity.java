package com.fitsoo.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fitsoo.activity.baseclass.BaseActivity;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.ProfileReponseModel;
import com.fitsoo.model.TermsAndPrivacy;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.utils.LocationUtil;
import com.fitsoo.utils.MultipartUtility;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fitsoo.R;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.preference.FitsooPref;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static com.fitsoo.activity.WelcomeActivity.IsfromLoginOrSignup;

public class LoginActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, OnReceiveResponseListener, FacebookCallback<LoginResult> {


    private static final int RC_SIGN_IN = 0;
    private String TAG = LoginActivity.class.getSimpleName();
    private TextView txtforgotpwd;
    private LinearLayout btnfacebook;
    private LinearLayout btnGoogle;
    private LinearLayout login_lyt_bottom;
    private Button btnLogin;
    private CallbackManager callbackManager;
    private EditText edtEmail;
    private ProgressDialog progressDialog;
    public static Boolean isfromLogin = false;
    private JSONObject fbIdEmail;
    private Profile fbProfile;
    private ProfileTracker mProfileTracker;
    private String refreshedToken;
    private LocationUtil util;
    private TextView txtTermsAndConditions;
    private BaseResponse<TermsAndPrivacy> termsAndPrivacy;
    private boolean isLoginActivityInFront = false;

    private EditText edtPassword;
    private GoogleSignInResult googleResult;
    //private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleApiClient;
    // Options for performing the Google login
    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("334126117462-5u32ug8kn1aieg5isj06jt9abk4imiem.apps.googleusercontent.com")
            .requestEmail()
            .build();

    public static final int FACEBOOK_SELECTION = 1;
    public static final int GOOGLE_SELECTION = 2;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Login Screen");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        isLoginActivityInFront = true;
        util = LocationUtil.getInstance(LoginActivity.this);
        util.startGettingLocation();

        if (util.getLastKnownLocation() != null) {
            FitsooPref.saveLocation(util.getLastKnownLocation().getLatitude(), util.getLastKnownLocation().getLongitude(), LoginActivity.this);
        }

        LoginManager.getInstance().logOut();
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        txtTermsAndConditions = (TextView) findViewById(R.id.txt_terms_and_conditions);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        txtforgotpwd = (TextView) findViewById(R.id.txtforgotpwd);
        btnfacebook = (LinearLayout) findViewById(R.id.btnfacebook);
        btnGoogle = (LinearLayout) findViewById(R.id.ll_google);
        btnLogin = (Button) findViewById(R.id.login_btnlogin);
        login_lyt_bottom = (LinearLayout) findViewById(R.id.login_lyt_bottom);

       /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this *//* FragmentActivity *//*, this *//* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/
        mGoogleApiClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);

        txtTermsAndConditions.setText(getTermsSpannableString());
        txtTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        FitsooUtils.performRequest(LoginActivity.this, "", getString(R.string.BASE_URL) + getString(R.string.cmspage), this, getString(R.string.str_please_wait), "cmspage");
        txtforgotpwd.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnfacebook.setOnClickListener(this);
        login_lyt_bottom.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtforgotpwd:
                passToForgotPassword();
                break;

            case R.id.btnfacebook:
                if (FitsooUtils.isInternetAvailable(this)) {
                    doFacebookLogin();
                } else {
                    FitsooUtils.showMessageAlert("Please check your internet connection.", getResources().getString(R.string.app_name), this);

                }
                break;

            case R.id.ll_google:
                /*Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);*/


                Intent signInIntent = mGoogleApiClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;

            case R.id.login_lyt_bottom:
                doSignup();
                break;

            case R.id.login_btnlogin:
                doLogin();
                break;

        }
    }

    private void passToForgotPassword() {
        Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(i);
        finish();
    }

    private void doLogin() {

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (checkLoginValidation()) {
            JSONObject loginReques = new JSONObject();
            try {
                loginReques.put(FitsooConstant.REQ_EMAIL, edtEmail.getText().toString().trim());
                loginReques.put(FitsooConstant.REQ_PASSWORD, edtPassword.getText().toString().trim());
                loginReques.put(FitsooConstant.REQ_GOOGLE_PLUS_ID, "");
                loginReques.put(FitsooConstant.REQ_LATITUDE, FitsooPref.getLatLong(LoginActivity.this)[0]);
                loginReques.put(FitsooConstant.REQ_LONGITUDE, FitsooPref.getLatLong(LoginActivity.this)[1]);
                loginReques.put(FitsooConstant.REQ_FACEBOOK_ID, "");
                loginReques.put(FitsooConstant.REQ_DEVICE_TOKEN, refreshedToken != null ? refreshedToken : FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "");
                loginReques.put(FitsooConstant.REQ_DEVICE_TYPE, "Android");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FitsooUtils.performRequest(LoginActivity.this, loginReques.toString(), getString(R.string.BASE_URL) + getString(R.string.login), this, getString(R.string.str_please_wait), "simplelogin");
        }
    }

    private void doSignup() {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(i);
        finish();
    }

    private boolean checkLoginValidation() {
        if (!FitsooUtils.hasValue(edtEmail.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_email_missing), getString(R.string.app_name), LoginActivity.this);
            return false;
        } else if (!FitsooUtils.isValidEmail(edtEmail.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_email_format), getString(R.string.app_name), LoginActivity.this);
            return false;
        } else if (!FitsooUtils.hasValue(edtPassword.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_password_missing), getString(R.string.app_name), LoginActivity.this);
            return false;
        }
        return true;
    }

    private void doFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void performFacebookLogin() {
        if (FitsooUtils.isInternetAvailable(this)) {
            JSONObject object = new JSONObject();
            try {
                object.put(FitsooConstant.REQ_FACEBOOK_ID, fbIdEmail.getString("id"));
                object.put(FitsooConstant.REQ_LATITUDE, FitsooPref.getLatLong(LoginActivity.this)[0]);
                object.put(FitsooConstant.REQ_LONGITUDE, FitsooPref.getLatLong(LoginActivity.this)[1]);
                object.put(FitsooConstant.REQ_DEVICE_TYPE, "Android");
                object.put(FitsooConstant.REQ_DEVICE_TOKEN, refreshedToken != null ? refreshedToken : FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "");
                FitsooUtils.performRequest(LoginActivity.this, object.toString(), getString(R.string.BASE_URL) + getString(R.string.login), this, getString(R.string.str_please_wait), "facebook");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            /*GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.getStatus().isSuccess()) {
                handleGoogleSignInResult(result);
            }*/
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            if (callbackManager != null)
                callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount googleResult = completedTask.getResult(ApiException.class);
            if (FitsooUtils.isInternetAvailable(this)) {
                JSONObject object = new JSONObject();
                try {
                    object.put(FitsooConstant.REQ_GOOGLE_PLUS_ID, googleResult.getId());
                    object.put(FitsooConstant.REQ_LATITUDE, FitsooPref.getLatLong(LoginActivity.this)[0]);
                    object.put(FitsooConstant.REQ_LONGITUDE, FitsooPref.getLatLong(LoginActivity.this)[1]);
                    object.put(FitsooConstant.REQ_DEVICE_TYPE, "Android");
                    object.put(FitsooConstant.REQ_DEVICE_TOKEN, refreshedToken != null ? refreshedToken : FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "");
                    FitsooUtils.performRequest(LoginActivity.this, object.toString(), getString(R.string.BASE_URL) + getString(R.string.login), this, getString(R.string.str_please_wait), "google");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (ApiException e) {
            e.printStackTrace();
            Log.d(TAG, "handleSignInResult: " + e.getMessage());
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.


        }
    }
    /*private void handleGoogleSignInResult(GoogleSignInResult result) {
        googleResult = result;

        if(FitsooUtils.isInternetAvailable(this)){
            JSONObject object = new JSONObject();
            try {
                object.put(FitsooConstant.REQ_GOOGLE_PLUS_ID, googleResult.getSignInAccount().getId());
                object.put(FitsooConstant.REQ_LATITUDE ,FitsooPref.getLatLong(LoginActivity.this)[0]);
                object.put(FitsooConstant.REQ_LONGITUDE , FitsooPref.getLatLong(LoginActivity.this)[1]);
                object.put(FitsooConstant.REQ_DEVICE_TYPE , "Android");
                object.put(FitsooConstant.REQ_DEVICE_TOKEN , refreshedToken != null ? refreshedToken : FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken():"");
                FitsooUtils.performRequest(LoginActivity.this , object.toString(),getString(R.string.BASE_URL) + getString(R.string.login), this, getString(R.string.str_please_wait),  "google");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResponseReceived(String requestType, String response) {
        switch (requestType) {
            case "simplelogin":
                proceedToLoginResponse(response);
                break;

            case "facebook":
                proceedToFacebookLogic(response);
                break;

            case "google":
                proceedToGoogleLogic(response);
                break;

            case "cmspage":
                processCMSPages(response);
                break;
        }
    }

    private void processCMSPages(String response) {
        termsAndPrivacy = (new Gson()).fromJson(response, new TypeToken<BaseResponse<TermsAndPrivacy>>() {
        }.getType());
        FitsooPref.setCMSresponse(LoginActivity.this, response);
    }

    private void proceedToGoogleLogic(String response) {
        FitsooUtils.dismissProgressDialog(progressDialog);
        BaseResponse<ProfileReponseModel> googleRes = (new Gson()).fromJson(response, new TypeToken<BaseResponse<ProfileReponseModel>>() {
        }.getType());
        if (googleRes.getSuccess() == 1) {
            FitsooPref.setUserId(LoginActivity.this, googleRes.getData().getId());
            FitsooPref.setUserProfileInfo(LoginActivity.this, response);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            if (FitsooUtils.isWriteStoragePermssionGranted(LoginActivity.this)) {
                submitData(GOOGLE_SELECTION, null);
            }
        }
    }

    private void proceedToFacebookLogic(String fbResponse) {
        BaseResponse<ProfileReponseModel> fbRes = (new Gson()).fromJson(fbResponse, new TypeToken<BaseResponse<ProfileReponseModel>>() {

        }.getType());
        if (fbRes.getSuccess() == 1) {
            FitsooPref.setUserId(LoginActivity.this, fbRes.getData().getId());
            FitsooPref.setUserProfileInfo(LoginActivity.this, fbResponse);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        } else {
            if (FitsooUtils.isWriteStoragePermssionGranted(LoginActivity.this)) {
                submitData(FACEBOOK_SELECTION, null);
            }
        }
    }

    private void proceedToLoginResponse(String resp) {
        BaseResponse<ProfileReponseModel> response = (new Gson()).fromJson(resp, new TypeToken<BaseResponse<ProfileReponseModel>>() {
        }.getType());
        if (response.getSuccess() == 1 || response.getSuccess() == 1001 || response.getSuccess() == 1002) {

            FitsooPref.setAccess(this, response.getSuccess());
            FitsooPref.setUserId(LoginActivity.this, response.getData().getId());
            FitsooPref.setUserProfileInfo(LoginActivity.this, resp);
            FitsooPref.setValidTill(LoginActivity.this, response.getData().getValid_till());
            FitsooPref.setSubscribeType(LoginActivity.this, response.getData().getSubscription_type());

            if (response.getData().getSubscription_type() == 0) {
//                isfromLogin = true;
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
//                Intent i = new Intent(LoginActivity.this, SubscribeActivity.class);
//                startActivity(i);
//                finish();
            } else {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            FitsooUtils.showMessageAlert(response.getMessage(), getString(R.string.app_name), this);
        }
    }

    private void passToWelcomeScreen(ProfileReponseModel model, String userProfileRes) {
        FitsooPref.setUserId(LoginActivity.this, model.getId());
        FitsooPref.setUserProfileInfo(LoginActivity.this, userProfileRes);
        Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        isLoginActivityInFront = true;
        super.onResume();
        IsfromLoginOrSignup = true;
    }

    @Override
    protected void onPause() {
        FitsooUtils.dismissProgressDialog(FitsooUtils.progressDialog);
        isLoginActivityInFront = false;
        super.onPause();
    }

    //stype == 1 facebook
    //stype == 2 Google
    private void submitData(final int stype, final File profilePic) {
        final AsyncTask<Void, String, String> waitForCompletion = new AsyncTask<Void, String, String>() {

            @Override
            protected void onPreExecute() {
                if (isLoginActivityInFront) {
                    progressDialog = FitsooUtils.showProgressDialog(LoginActivity.this, getString(R.string.str_please_wait));
                }
                refreshedToken = FirebaseInstanceId.getInstance().getToken();
            }

            @Override
            public synchronized String doInBackground(Void... params) {
                String res = "";
                String charset = "UTF-8";
                String requestURL = getString(R.string.BASE_URL) + getString(R.string.signup);
                try {
                    MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                    multipart.addFormField("email", stype == 1 ? fbIdEmail.getString("email") : googleResult.getSignInAccount().getEmail());
                    multipart.addFormField("password", "");
                    multipart.addFormField("first_name", stype == 1 ? fbProfile.getFirstName() : googleResult.getSignInAccount().getGivenName() != null ? googleResult.getSignInAccount().getGivenName() : "Fitsoo");
                    multipart.addFormField("last_name", stype == 1 ? fbProfile.getLastName() : googleResult.getSignInAccount().getFamilyName() != null ? googleResult.getSignInAccount().getFamilyName() : "User");
                    multipart.addFormField("dob", stype == 1 ? "" : "");
                    multipart.addFormField(FitsooConstant.REQ_LATITUDE, FitsooPref.getLatLong(LoginActivity.this)[0]);
                    multipart.addFormField(FitsooConstant.REQ_LONGITUDE, FitsooPref.getLatLong(LoginActivity.this)[1]);
                    multipart.addFormField("fb_id", stype == 1 ? fbIdEmail.getString("id") : "");
                    multipart.addFormField("gp_id", stype == 1 ? "" : googleResult.getSignInAccount().getId());
                    multipart.addFormField("device_token", FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "");
                    multipart.addFormField("device_type", "ANDROID");
                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (profilePic != null) {
                            multipart.addFilePart("profile_pic", profilePic);
                        } else {
                            if (stype == 1) {
                                multipart.addFilePart("profile_pic", new File(FitsooUtils.saveImage(getFacebookProfilePicture(fbIdEmail.getString("id")), FitsooConstant.PROFILE_IMAGE_NAME)));
                            } else {
                                if ((new File(FitsooUtils.saveImage(getGoogleProfilePicture(String.valueOf(googleResult.getSignInAccount().getPhotoUrl())), FitsooConstant.PROFILE_IMAGE_NAME)) != null)) {
                                    if (googleResult.getSignInAccount().getPhotoUrl() == null) {
                                        multipart.addFormField("profile_pic", "");
                                    } else {
                                        multipart.addFilePart("profile_pic", new File(FitsooUtils.saveImage(getGoogleProfilePicture(String.valueOf(googleResult.getSignInAccount().getPhotoUrl())), FitsooConstant.PROFILE_IMAGE_NAME)));
                                    }
                                } else {
                                    String path = FitsooUtils.saveImage(BitmapFactory.decodeResource(getResources(), R.mipmap.profile_avtar), FitsooConstant.PROFILE_IMAGE_NAME);
                                    if (path != null) {
                                        multipart.addFilePart("profile_pic", (new File(path)));
                                    } else {
                                        multipart.addFormField("profile_pic", "");
                                    }
                                }
                            }
                        }
                    } else {
                        multipart.addFormField("profile_pic", "");
                    }

                    List<String> response = multipart.finish();

                    for (String line : response) {
                        System.out.println(line);
                        res = line;
                    }
                } catch (IOException ex) {
                    System.err.println(ex);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return res;

            }

            @Override
            protected void onPostExecute(String result) {
                FitsooUtils.dismissProgressDialog(progressDialog);
                BaseResponse<ProfileReponseModel> resModel = (new Gson()).fromJson(result, new TypeToken<BaseResponse<ProfileReponseModel>>() {
                }.getType());
                if (resModel != null) {
                    if (resModel.getSuccess() == 1) {
                        passToWelcomeScreen(resModel.getData(), result);
                    } else {
                        FitsooUtils.showMessageAlert(resModel.getMessage(), getString(R.string.app_name), LoginActivity.this);
                    }
                } else {
                    FitsooUtils.showMessageAlert("There was some error signing up,\nPlease try again in some time.", getString(R.string.app_name), LoginActivity.this);
                }
            }
        };

        if (FitsooUtils.isInternetAvailable(LoginActivity.this)) {
            waitForCompletion.execute();
        } else {
            //TODO show internet not available
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        if (Profile.getCurrentProfile() == null) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    // profile2 is the new profile
                    Log.v("facebook - profile", profile2.getFirstName());
                    fbProfile = profile2;
                    mProfileTracker.stopTracking();
                }
            };
        } else {
            fbProfile = Profile.getCurrentProfile();
        }
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        Log.v("facebook--", json_object.toString());
                        fbIdEmail = json_object;
                        performFacebookLogin();
                    }
                });

        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,email");
        // Below is the sample permission to get different datas might need to update it this way
//        permission_param.putString("fields", "id,about,age_range,birthday,cover,education,email,first_name,gender,last_name,locale,location,name,work,website,verified,updated_time,timezone,sports,religion,relationship_status");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }


    private SpannableString getTermsSpannableString() {
        SpannableString ssPrivacy = new SpannableString("By Continuing you accept our \nPrivacy Policy and Terms of Service");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(termsAndPrivacy.getData().getPrivacy()));
                startActivity(i);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(LoginActivity.this, R.color.textcolor));
            }
        };
        ssPrivacy.setSpan(clickableSpan, 30, 44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickTerms = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(termsAndPrivacy.getData().getTerms()));
                startActivity(i);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(LoginActivity.this, R.color.textcolor));
            }
        };
        ssPrivacy.setSpan(clickTerms, 49, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssPrivacy;
    }


    public static Bitmap getFacebookProfilePicture(String userID) {
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap getGoogleProfilePicture(String url) {
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            imageURL = new URL(url);
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


}
