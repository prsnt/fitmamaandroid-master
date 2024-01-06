package com.fitsoo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatDelegate;

import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.PermissionUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.fitsoo.activity.baseclass.BaseActivity;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.interfacepack.DateSelectionDialog;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.ProfileReponseModel;
import com.fitsoo.model.TermsAndPrivacy;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.DatePickerFragment;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.utils.LocationUtil;
import com.fitsoo.utils.MultipartUtility;
import com.fitsoo.view.CustomButton;
import com.fitsoo.view.CustomTextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignupActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, DateSelectionDialog, FacebookCallback<LoginResult>, OnReceiveResponseListener {

    private static final int RC_SIGN_IN = 0;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_PERMISSION_SETTING = 1233;
    private String TAG = SignupActivity.class.getSimpleName();
    private LinearLayout llLoginPageButton;
    private CustomButton btnSignUp;
    private CircularImageView profImage;
    private String path;

    private ImageView imgEdit;
    private ImageView imgBack;
    private ImageView imgHelp;
    private ProgressDialog progressDialog;

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtFirstName;
    private EditText edtLastName;
    private CustomTextView edt_selectLanguage;
    private TextView txtDateOfBirth;

    private LinearLayout btnFacebook;
    private LinearLayout btnGoogle;

    private CallbackManager callbackManager;

    private JSONObject fbIdEmail;
    private Profile fbProfile;
    private ProfileTracker mProfileTracker;

    RadioButton rbtnEnglish;
    RadioButton rbtnHebrew;

    public static final int FACEBOOK_SELECTION = 1;
    public static final int GOOGLE_SELECTION = 2;
    private TextView txtTermsAndConditions;

    private GoogleSignInAccount googleResult;
    //private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleApiClient;

    private String refreshedToken;
    private LocationUtil util;
    // Options for performing the Google login
    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

    private Uri mCropImageUri;
    private Bitmap myBitmap;
    private BaseResponse<TermsAndPrivacy> termsAndPrivacy;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Spinner spinnerLanguage;
    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawableResource(R.mipmap.signup);
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "SignUp Screen");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        util = LocationUtil.getInstance(SignupActivity.this);
        util.startGettingLocation();

        if (util.getLastKnownLocation() != null) {
            FitsooPref.saveLocation(util.getLastKnownLocation().getLatitude(), util.getLastKnownLocation().getLongitude(), SignupActivity.this);
        }
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        txtTermsAndConditions = (TextView) findViewById(R.id.txt_terms_and_conditions);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtFirstName = (EditText) findViewById(R.id.edt_firstname);
        edt_selectLanguage = (CustomTextView) findViewById(R.id.edt_selectLanguage);
        edtLastName = (EditText) findViewById(R.id.edt_lastname);
        txtDateOfBirth = (TextView) findViewById(R.id.txt_date_of_birth);
        rbtnHebrew = (RadioButton) findViewById(R.id.rbtnHebrew);
        rbtnEnglish = (RadioButton) findViewById(R.id.rbtnEnglish);

        btnGoogle = (LinearLayout) findViewById(R.id.btnGoogle);
        btnFacebook = (LinearLayout) findViewById(R.id.btnfacebook);

        profImage = (CircularImageView) findViewById(R.id.img_profile);
        btnSignUp = (CustomButton) findViewById(R.id.btn_sign_up);
        imgEdit = (ImageView) findViewById(R.id.img_edit);
        imgBack = (ImageView) findViewById(R.id.signup_img_goback);
        imgHelp = (ImageView) findViewById(R.id.imgHelp);
        llLoginPageButton = (LinearLayout) findViewById(R.id.signup_lyt_bottom);

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this *//* FragmentActivity *//*, this *//* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/
        mGoogleApiClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);

        txtTermsAndConditions.setText(getTermsSpannableString());
        txtTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        FitsooUtils.performRequest(SignupActivity.this, "", getString(R.string.BASE_URL) + getString(R.string.cmspage), this, getString(R.string.str_please_wait), "cmspage");

        txtDateOfBirth.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        llLoginPageButton.setOnClickListener(this);
        edt_selectLanguage.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        imgHelp.setOnClickListener(this);
    }

    private void selectLanguage() {
        final List<String> list = new ArrayList<>();
        list.add("English");
        list.add("Hebrew");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_language, null, false);
        ListView listview_language = view.findViewById(R.id.listview_language);
        listview_language.setAdapter(new ArrayAdapter<String>(this, R.layout.item_list, list));
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        listview_language.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edt_selectLanguage.setText(list.get(position));
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_selectLanguage:
                //selectLanguage();
                break;
            case R.id.btnGoogle:
                Intent signInIntent = mGoogleApiClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;

            case R.id.btnfacebook:
                doFacebookLogin();
                break;

            case R.id.signup_img_goback:
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                SignupActivity.this.finish();
                break;

            case R.id.signup_lyt_bottom:
                Intent intentLogin = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                SignupActivity.this.finish();
                break;

            case R.id.img_edit:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }


                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    LogUtil.logd("TAG", "denied" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        performProfileAction();
//                        LogUtil.logd("TAG", "allowed" + permission);
                    } else {
//                        LogUtil.logd("TAG", "set to never ask again" + permission);
//                        somePermissionsForeverDenied = true;
                        showAlertWithTwoButton(this, "Please go to setting and allow storage permission");
                    }
                }
                break;

            case R.id.txt_date_of_birth:
                performDateSelection();
                break;

            case R.id.imgHelp:
                FitsooUtils.showMessageAlert(getString(R.string.str_help), getString(R.string.app_name), SignupActivity.this);
                break;
            case R.id.btn_sign_up:

                if (path == null || path.length() <= 0) {
                    path = FitsooUtils.saveImage(BitmapFactory.decodeResource(getResources(), R.mipmap.profile_avtar), FitsooConstant.PROFILE_IMAGE_NAME);
                }

                if (checkSignUpValidation()) {
                    submitRegisterData(edtEmail.getText().toString().trim()
                            , edtPassword.getText().toString().trim(), edtFirstName.getText().toString().trim()
                            , edtLastName.getText().toString().trim(), txtDateOfBirth.getText().toString().trim()
                            , path.length() > 0 ? new File(path) : null);
                }
                break;
        }
    }

    private void performDateSelection() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setDateListener(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void performProfileAction() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                }
            } else {
                CropImage.startPickImageActivity(this);
            }
        } else {
            CropImage.startPickImageActivity(this);
        }
    }

    private void doFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Glide.with(this).asBitmap().load(result.getUri()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        profImage.setImageBitmap(resource);
                        path = FitsooUtils.saveImage(resource, FitsooConstant.PROFILE_IMAGE_NAME);
                        myBitmap = resource;
                    }
                });
                /*Glide.with(this).asBitmap().load(result.getUri()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        profImage.setImageBitmap(resource);
                        path = FitsooUtils.saveImage(resource, FitsooConstant.PROFILE_IMAGE_NAME);
                        myBitmap = resource;
                    }
                });*/
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RC_SIGN_IN) {
           /* GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
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
            googleResult = completedTask.getResult(ApiException.class);
            if (FitsooUtils.isWriteStoragePermssionGranted(SignupActivity.this) && FitsooUtils.isInternetAvailable(this)) {
                path = FitsooUtils.saveImage(BitmapFactory.decodeResource(getResources(), R.mipmap.profile_avtar), FitsooConstant.PROFILE_IMAGE_NAME);
                submitSocialData(GOOGLE_SELECTION, new File(path));
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.


        }
    }
   /* private void handleGoogleSignInResult(GoogleSignInResult result) {
        googleResult = result;
        if(FitsooUtils.isWriteStoragePermssionGranted(SignupActivity.this) && FitsooUtils.isInternetAvailable(this)){
            path = FitsooUtils.saveImage(BitmapFactory.decodeResource(getResources(), R.mipmap.profile_avtar), FitsooConstant.PROFILE_IMAGE_NAME);
            submitSocialData(GOOGLE_SELECTION, new File(path));
        }
    }*/


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(10, 10)
                .setMultiTouchEnabled(true)
                .start(this);
    }


    private boolean checkSignUpValidation() {
        if (!FitsooUtils.hasValue(edtEmail.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_email_missing), getString(R.string.app_name), SignupActivity.this);
            return false;
        } else if (!FitsooUtils.isValidEmail(edtEmail.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_email_format), getString(R.string.app_name), SignupActivity.this);
            return false;
        } else if (!FitsooUtils.hasValue(edtPassword.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_password_missing), getString(R.string.app_name), SignupActivity.this);
            return false;
        } else if (!FitsooUtils.isOfMinimumLength(edtPassword.getText().toString().trim(), FitsooConstant.PASSWORD_LENGTH)
                || !FitsooUtils.isExpressionCorrect(edtPassword.getText().toString().trim(), FitsooConstant.PASSWORD_PATTERN)) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_password_length), getString(R.string.app_name), SignupActivity.this);
            return false;
        } else if (!FitsooUtils.hasValue(edtFirstName.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_first_name), getString(R.string.app_name), SignupActivity.this);
            return false;
        } else if (!FitsooUtils.hasValue(edtLastName.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_last_name), getString(R.string.app_name), SignupActivity.this);
            return false;
        } else if (!FitsooUtils.hasValue(txtDateOfBirth.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_error_dob), getString(R.string.app_name), SignupActivity.this);
            return false;
        }

        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDateSelected(DatePicker view, int year, int month, int day) {
        txtDateOfBirth.setText(month + 1 + "-" + day + "-" + year);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        if (Profile.getCurrentProfile() == null) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    // profile2 is the new profile
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
                        fbIdEmail = json_object;
                        if (FitsooUtils.isWriteStoragePermssionGranted(SignupActivity.this) && FitsooUtils.isInternetAvailable(SignupActivity.this)) {
                            submitSocialData(FACEBOOK_SELECTION, null);
                        }
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


    //stype == 1 facebook
    //stype == 2 Google
    private void submitSocialData(final int stype, final File profilePic) {
        final AsyncTask<Void, String, String> waitForCompletion = new AsyncTask<Void, String, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = FitsooUtils.showProgressDialog(SignupActivity.this, getString(R.string.str_please_wait));
            }

            @Override
            public synchronized String doInBackground(Void... params) {
                String res = "";
                String charset = "UTF-8";
                String requestURL = getString(R.string.BASE_URL) + getString(R.string.signup);

                try {

                    MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                    multipart.addFormField("email", stype == 1 ? fbIdEmail.getString("email") : googleResult.getEmail());
                    multipart.addFormField("password", "");
                    multipart.addFormField("first_name", stype == 1 ? fbProfile.getFirstName() : googleResult.getGivenName());
                    multipart.addFormField("last_name", stype == 1 ? fbProfile.getLastName() : googleResult.getFamilyName());
                    multipart.addFormField("dob", "");
                    multipart.addFormField(FitsooConstant.REQ_LATITUDE, FitsooPref.getLatLong(SignupActivity.this)[0]);
                    multipart.addFormField(FitsooConstant.REQ_LONGITUDE, FitsooPref.getLatLong(SignupActivity.this)[1]);
                    multipart.addFormField("fb_id", stype == 1 ? fbIdEmail.getString("id") : "");
                    multipart.addFormField("gp_id", stype == 1 ? "" : googleResult.getId());
                    multipart.addFormField("language", edt_selectLanguage.getText().toString().contains("English") ? "0" : "1");
                    multipart.addFormField("device_token", FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "");
                    multipart.addFormField("device_type", "ANDROID");
                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (profilePic != null) {
                            multipart.addFilePart("profile_pic", profilePic);
                        } else {
                            if (stype == 1) {
                                multipart.addFilePart("profile_pic", new File(FitsooUtils.saveImage(getFacebookProfilePicture(fbIdEmail.getString("id")), FitsooConstant.PROFILE_IMAGE_NAME)));
                            } else {
                                if ((new File(FitsooUtils.saveImage(getGoogleProfilePicture(String.valueOf(googleResult.getPhotoUrl())), FitsooConstant.PROFILE_IMAGE_NAME)) != null)) {
                                    multipart.addFilePart("profile_pic", new File(FitsooUtils.saveImage(getGoogleProfilePicture(String.valueOf(googleResult.getPhotoUrl())), FitsooConstant.PROFILE_IMAGE_NAME)));
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
                        passToHomeScreen(resModel.getData(), result);
                    } else {
                        FitsooUtils.showMessageAlert(resModel.getMessage(), getString(R.string.app_name), SignupActivity.this);
                    }
                } else {
                    FitsooUtils.showMessageAlert("There was some error signing up,\nPlease try again in some time.", getString(R.string.app_name), SignupActivity.this);
                }
            }
        };

        if (FitsooUtils.isInternetAvailable(SignupActivity.this)) {
            waitForCompletion.execute();
        } else {
            //TODO show internet not available
        }
    }


    private void submitRegisterData(final String email, final String password, final String fname, final String lname, final String dob, final File profilePic) {
        final AsyncTask<Void, String, String> waitForCompletion = new AsyncTask<Void, String, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = FitsooUtils.showProgressDialog(SignupActivity.this, getString(R.string.str_please_wait));
            }

            @Override
            public synchronized String doInBackground(Void... params) {
                String res = "";
                String charset = "UTF-8";
                String requestURL = getString(R.string.BASE_URL) + getString(R.string.signup);

                try {
                    MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                    multipart.addFormField("email", email);
                    multipart.addFormField("password", password);
                    multipart.addFormField("first_name", fname);
                    multipart.addFormField("last_name", lname);
                    multipart.addFormField("dob", dob);
                    multipart.addFormField("fb_id", "");
                    multipart.addFormField(FitsooConstant.REQ_LATITUDE, FitsooPref.getLatLong(SignupActivity.this)[0]);
                    multipart.addFormField(FitsooConstant.REQ_LONGITUDE, FitsooPref.getLatLong(SignupActivity.this)[1]);
                    multipart.addFormField("gp_id", "");
                    if (rbtnEnglish.isChecked())
                        multipart.addFormField("language", "0");
                    else
                        multipart.addFormField("language", "1");
                    multipart.addFormField("device_token", FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "");
                    multipart.addFormField("device_type", "ANDROID");
                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (profilePic != null) {
                            multipart.addFilePart("profile_pic", profilePic);
                        } else {
                            multipart.addFormField("profile_pic", "");
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
                        passToHomeScreen(resModel.getData(), result);
                    } else {
                        FitsooUtils.showMessageAlert(resModel.getMessage(), getString(R.string.app_name), SignupActivity.this);
                    }
                } else {
                    FitsooUtils.showMessageAlert("There was some error signing up,\nPlease try again in some time.", getString(R.string.app_name), SignupActivity.this);
                }
            }
        };

        if (FitsooUtils.isInternetAvailable(SignupActivity.this)) {
            waitForCompletion.execute();
        } else {
            //TODO show internet not available
        }
    }


    private void passToHomeScreen(ProfileReponseModel model, String strProfileResponse) {
        FitsooPref.setUserId(SignupActivity.this, model.getId());
        FitsooPref.setUserProfileInfo(SignupActivity.this, strProfileResponse);
        BaseResponse response = (new Gson()).fromJson(strProfileResponse, new TypeToken<BaseResponse>() {
        }.getType());
        if (response.getMessage().contains("Login")) {
            Intent i = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(SignupActivity.this, WelcomeActivity.class);
            startActivity(i);
            finish();
        }
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
                ds.setColor(ContextCompat.getColor(SignupActivity.this, R.color.textcolor));
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
                ds.setColor(ContextCompat.getColor(SignupActivity.this, R.color.textcolor));
            }
        };
        ssPrivacy.setSpan(clickTerms, 49, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssPrivacy;

    }

    @Override
    public void onResponseReceived(String requestType, String response) {
        switch (requestType) {

            case "cmspage":
                processCMSPages(response);
                break;
        }
    }


    private void processCMSPages(String response) {
        termsAndPrivacy = (new Gson()).fromJson(response, new TypeToken<BaseResponse<TermsAndPrivacy>>() {
        }.getType());
        FitsooPref.setCMSresponse(SignupActivity.this, response);
    }

    @Override
    public void onBackPressed() {
        Intent passToLogin = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(passToLogin);
        finish();
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

    @Override
    protected void onPause() {
        FitsooUtils.dismissProgressDialog(FitsooUtils.progressDialog);
        super.onPause();
    }


    public static void showAlertWithTwoButton(final Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", activity.getPackageName(), null));
                activity.startActivityForResult(intent, 1200);
            }
        });
        builder.show();
    }
}
