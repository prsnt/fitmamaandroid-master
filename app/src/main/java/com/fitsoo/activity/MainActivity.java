package com.fitsoo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.facebook.login.LoginManager;
import com.fitsoo.R;
import com.fitsoo.activity.baseclass.BaseActivity;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.fragment.ChallengeFragment;
import com.fitsoo.fragment.ContactUsFragment;
import com.fitsoo.fragment.HomeFragment;
import com.fitsoo.fragment.MoreFragment;
import com.fitsoo.fragment.ProfileFragment;
import com.fitsoo.fragment.ProgramExtraFragment;
import com.fitsoo.fragment.ProgramInnerFragment;
import com.fitsoo.fragment.ProgramsFragment;
import com.fitsoo.fragment.WorkOutFragment;
import com.fitsoo.interfacepack.CustomDialogTwoButtonListener;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.TermsAndPrivacy;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.utils.FontsOverride;
import com.fitsoo.utils.LocationUtil;
import com.fitsoo.utils.inappsecurity.Security;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, PurchasesUpdatedListener {
    public static final String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjV+nh0Ngy+Soy62nxPtWJo/jakzcRJF+2ZVHLvVeepKdcGFQ2EyliovI8J7Hv6GNXn04KhzC0QgLqbgC9hawsdtzwWNpC+p5E9N3aKpgiS6GSEEGsW7AhUUfjmcX+cmVubI3rn7QClJPKD81ufgX7g/RYIWgQS7jV1hQpnnqzkFqqrMlEEVrdXj20FIRdo42nGpTXJeXHjbwCdlYlJTy/OzEfWCn35hSeJRLy8K6fYL1OugDiIlJBkVlqKByFdB+wzfA30omrrffvJ+PZiJRbzJE+4MmJ+/fatbvST30FV1hD9hhfT3A5d/7sv8J0aU3fr7hpH26FmXXYJtk2rbAvwIDAQAB";
    BottomNavigationView bottomNavigation;
    private Toolbar toolbar;
    private ImageView imgClose;
    private ImageView imgHamburger;
    private ImageView imgShare;
    private TextView txtTitle;
    private LocationUtil util;
    private BillingClient mBillingClient;
    private ConsumeResponseListener listener;
    private int vTempPosition = 3;
    private FirebaseAnalytics mFirebaseAnalytics;
    private GoogleApiClient mGoogleApiClient;
    // Options for performing the Google login
    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    private boolean isPurchased;
    private boolean dontNavigate;
    BottomNavigationView.OnNavigationItemSelectedListener bottomlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(FitsooUtils.FRAGMENT_TRANSITION_TAG);
            Log.e("click", "addLogOfTime: ");
//            addLogOfTime(MainActivity.this);

            switch (menuItem.getItemId()) {

                case R.id.action_Home:

                    if (!dontNavigate) {
                        if (!(fragment instanceof HomeFragment)) {
                            FitsooUtils.ChangeFragment(false, null, new HomeFragment(), MainActivity.this);
                        }
                    }
                    dontNavigate = false;
                    break;
                case R.id.action_Challange:
                    if (!dontNavigate) {
                        if (!(fragment instanceof ChallengeFragment)) {
                            FitsooUtils.ChangeFragment(false, null, new ChallengeFragment(), MainActivity.this);
                        }
                    }
                    dontNavigate = false;
                    break;

                case R.id.action_Workout:
                    if (!dontNavigate) {
                        if (!(fragment instanceof WorkOutFragment)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("position", "" + FitsooUtils.vidPosition);
                            FitsooUtils.ChangeFragment(false, bundle, new WorkOutFragment(), MainActivity.this);
                        }
                    }
                    dontNavigate = false;
                    break;

                case R.id.action_programs:
                    if (!dontNavigate) {
                        if (!(fragment instanceof ProgramsFragment)) {
                            FitsooUtils.ChangeFragment(false, null, new ProgramsFragment(), MainActivity.this);
                        }
                    }
                    dontNavigate = false;
                    break;

                case R.id.action_more:
                    if (!dontNavigate) {
                        if (!(fragment instanceof MoreFragment)) {
                            FitsooUtils.ChangeFragment(false, null, new MoreFragment(), MainActivity.this);
                        }
                    }
                    dontNavigate = false;
                    break;
            }
            return true;
        }
    };
    private Fragment mContent;

    @Override
    protected void onResume() {


        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
            }
        }, 500);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "FragTrans");
        }

        mBillingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();

        mBillingClient.startConnection(new BillingClientStateListener() {

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                Purchase.PurchasesResult result;
                result = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
                Log.e("abc", "onBillingSetupFinished: " + result);

                mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS,
                        new PurchaseHistoryResponseListener() {
                            @Override
                            public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> list) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                        && list != null) {
                                    try {
                                        for (PurchaseHistoryRecord purchase : list) {
                                            FitsooPref.setString(MainActivity.this, purchase.getOriginalJson());
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }
                        });
            }

            @Override
            public void onBillingServiceDisconnected() {
                //                Timber.d("something went wrong ");
            }
        });


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //setInitialBillingInfo();
        util = LocationUtil.getInstance(MainActivity.this);
        util.startGettingLocation();

        if (util.getLastKnownLocation() != null) {
            FitsooPref.saveLocation(util.getLastKnownLocation().getLatitude(), util.getLastKnownLocation().getLongitude(), MainActivity.this);
        }
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TextView) findViewById(R.id.menu_title);
        imgHamburger = (ImageView) findViewById(R.id.menu_hamburger);
        imgShare = (ImageView) findViewById(R.id.menu_share);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(bottomlistener);

        imgHamburger.setOnClickListener(this);
        imgShare.setOnClickListener(this);

        setSupportActionBar(toolbar);

        if (FitsooUtils.fragName.equalsIgnoreCase(getString(R.string.nav_workout))) {
            updateTitle(getString(R.string.nav_workout));
            Bundle bundle = new Bundle();
            bundle.putString("position", "" + FitsooUtils.vidPosition);
            FitsooUtils.ChangeFragment(false, bundle, new WorkOutFragment(), this);
        } else if (FitsooUtils.fragName.equalsIgnoreCase(getString(R.string.programInner))) {
            updateTitle(getString(R.string.programs));
            Bundle bundle = new Bundle();
            bundle.putParcelable("model", FitsooUtils.programHomeModel);
            FitsooUtils.ChangeFragment(false, bundle, new ProgramInnerFragment(), this);
        } else if (FitsooUtils.fragName.equalsIgnoreCase(getString(R.string.programExtra))) {
            updateTitle(getString(R.string.programs));
            FitsooUtils.ChangeFragment(false, null, new ProgramExtraFragment(), this);
        } else if (FitsooUtils.fragName.equalsIgnoreCase(getString(R.string.nav_profile))) {
            loadProfile();
            Log.d("Mainactivity", "onCreate: Mainactivity");
        }/* else if (FitsooUtils.fragName.equalsIgnoreCase(getString(R.string.programInner))) {
            loadProgramInner();
        } else if (FitsooUtils.fragName.equalsIgnoreCase(getString(R.string.programExtra))) {
            loadProgramExtra();
        } else if (FitsooUtils.fragName.equalsIgnoreCase(getString(R.string.programs))) {
            loadPrograms();
        } */ else
            loadHomeScreen();


        //loadHomeScreen();

        FontsOverride.setDefaultFont(MainActivity.this, "MONOSPACE");

        addLogOfTime(this);

    }

    public void createLogInAnalytics(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, screenName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void BackVisible(boolean isVisible) {
        if (isVisible)
            imgHamburger.setVisibility(View.VISIBLE);
        else
            imgHamburger.setVisibility(View.GONE);
    }

    private void reloadSamePage() {
        FitsooUtils.fragName = "";
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void performLogOut() {

        FitsooUtils.showMessageAlert(getString(R.string.str_logout_message), getString(R.string.app_name), this, new CustomDialogTwoButtonListener() {
            @Override
            public void onDialogPositiveButtonClick(DialogInterface dialog, int which) {
                FitsooUtils.fragName = "";
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                    mGoogleApiClient.disconnect();
                }
                LoginManager.getInstance().logOut();
                FitsooPref.clearSharePreference(MainActivity.this);
                Intent passToLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(passToLogin);
                finish();
            }

            @Override
            public void onDialogNegativeButtonClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, "Yes", "No");

    }
//
//    private void loadWorkOut() {
//        BaseResponse<List<WorkoutModel>> workoutBaseRes = (new Gson()).fromJson(FitsooPref.getWorkOutRes(MainActivity.this), new TypeToken<BaseResponse<List<WorkoutModel>>>() {
//        }.getType());
//        if (workoutBaseRes.getSuccess() == 1) {
//            updateTitle(getString(R.string.nav_workout));
//            Bundle bundle = new Bundle();
//            bundle.putString("position", "3");
//            Fragment workOutFragment = new WorkOutFragment();
//            workOutFragment.setArguments(bundle);
//            FragmentManager workOutFrag = getSupportFragmentManager();
//            workOutFrag.beginTransaction().replace(R.id.content_frame, workOutFragment, FitsooUtils.FRAGMENT_TRANSITION_TAG).commit();
//        } else {
//            FitsooUtils.showMessageAlert(workoutBaseRes.getMessage() , getString(R.string.app_name) , MainActivity.this);
//        }
//    }

    private void loadChallenge() {
        updateTitle(getString(R.string.nav_challenge));
        FitsooUtils.ChangeFragment(false, null, new ChallengeFragment(), this);
    }

    private void loadProfile() {
        updateTitle(getString(R.string.nav_profile));
        FitsooUtils.ChangeFragment(false, null, new ProfileFragment(), this);
    }

    private void loadPrograms() {
        updateTitle(getString(R.string.programs));
        FitsooUtils.ChangeFragment(false, null, new ProgramsFragment(), this);
    }

    private void loadProgramInner() {
        updateTitle(getString(R.string.programs));
        FitsooUtils.ChangeFragment(false, null, new ProgramInnerFragment(), this);
    }

    private void loadProgramExtra() {
        updateTitle(getString(R.string.programs));
        FitsooUtils.ChangeFragment(false, null, new ProgramExtraFragment(), this);
    }

    private void loadHomeScreen() {
        updateTitle(getString(R.string.nav_home));
        FitsooUtils.ChangeFragment(false, null, new HomeFragment(), this);
    }

    private void loadContactUs() {
        updateTitle(getString(R.string.contact_us));
        FitsooUtils.ChangeFragment(false, null, new ContactUsFragment(), this);
    }

    private void loadChangePassword() {
        Intent passToChangePassword = new Intent(MainActivity.this, ChangePassword.class);
        startActivity(passToChangePassword);
        finish();
    }

    public void updateTitle(String title) {
        txtTitle.setText(title);
    }

    public void updateBottomSelection(int id) {
        dontNavigate = true;
        bottomNavigation.setSelectedItemId(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_share:
                shareMethod();
                break;

            case R.id.menu_hamburger:
                FitsooUtils.hideSoftKeyboard(MainActivity.this);
                onBackPressed();
                break;
        }
    }

    private void shareMethod() {
        FitsooUtils.performRequest(MainActivity.this, "", getString(R.string.BASE_URL) + getString(R.string.cmspage), new OnReceiveResponseListener() {
            @Override
            public void onResponseReceived(String requestType, String response) {
                Gson gson = new Gson();
                BaseResponse<TermsAndPrivacy> termsAndPrivacy = (gson).fromJson(response, new TypeToken<BaseResponse<TermsAndPrivacy>>() {
                }.getType());
                FitsooPref.setCMSresponse(MainActivity.this, response);
                if (!FitsooUtils.hasValue(termsAndPrivacy.getData().getAndroid())) {
                    String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Check this new Fitness app, It's awesome!. \nTo check it out click the URL given below!\n" + url);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                } else {
                    String url = termsAndPrivacy.getData().getAndroid();
//                    Log.e("ShareData", "onResponseReceived: "+response+":::::"+termsAndPrivacy.getData().getAndroid());
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }
        }, getString(R.string.str_please_wait), "");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FitsooUtils.FRAGMENT_TRANSITION_TAG);

        if (fragment instanceof HomeFragment) {
            FitsooUtils.showMessageAlert(getString(R.string.str_close_app_message), getString(R.string.app_name), this, new CustomDialogTwoButtonListener() {
                @Override
                public void onDialogPositiveButtonClick(DialogInterface dialog, int which) {
                    closeTheApplication();
                }

                @Override
                public void onDialogNegativeButtonClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, "Yes", "No");
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getSupportFragmentManager().findFragmentByTag(FitsooUtils.FRAGMENT_TRANSITION_TAG) instanceof ProfileFragment) {
            Fragment profFrag = getSupportFragmentManager().findFragmentByTag(FitsooUtils.FRAGMENT_TRANSITION_TAG);
            profFrag.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void closeTheApplication() {
        FitsooUtils.fragName = "";
        super.onBackPressed();
    }

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            return Security.verifyPurchase(BASE_64_ENCODED_PUBLIC_KEY, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

    // TODO verify the Signature if it was ok
    private void loadWorkoutFragment(int position) {
        isPurchased = true;
        Bundle bundle = new Bundle();
        bundle.putString("position", "" + position);
        FitsooUtils.vidPosition = position;
        FitsooUtils.ChangeFragment(true, bundle, new WorkOutFragment(), this);
        updateBottomSelection(R.id.action_Workout);
    }

    @Override
    protected void onPause() {
        FitsooUtils.dismissProgressDialog(FitsooUtils.progressDialog);
        super.onPause();
    }

    void addLogOfTime(final Context context) {

        JSONObject loginReques = new JSONObject();
        try {
            loginReques.put(FitsooConstant.REQ_USER_ID, FitsooPref.getUserId(context));
            loginReques.put("timezone", Calendar.getInstance().getTimeZone().getID());
            loginReques.put("hourUsageResponse", FitsooPref.getString(context));
            loginReques.put("device_type", "Android");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FitsooUtils.performRequestReceiver(context, loginReques.toString(), context.getString(R.string.BASE_URL) + context.getString(R.string.gethoursusage), new OnReceiveResponseListener() {
            @Override
            public void onResponseReceived(String requestType, String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    FitsooPref.setAccess(context, object.getInt("success"));
                    FitsooPref.setAccessmsg(context, object.getString("message"));
                    JSONObject object1 = object.getJSONObject("data");

                    FitsooPref.setPurchaseToken(context, object1.getString("purchaseToken"));
                    FitsooPref.setisNewUser(context, object1.getString("isNewUser"));
                    FitsooPref.setpackage_bought_from(context, object1.getString("package_bought_from"));

//                    if(object1.getString("isNewUser").equals("0") ) {
//                        if (isopenAppForFistTime == 0) {
//                            FitsooUtils.showMessageAlert("Hello Users,\n" +
//                                    "\n" +
//                                    "we have an updates in our subscription packages as well as we have introduced few more attractive features so your subscription will get updated based on new subscription prices.\n" +
//                                    "\n" +
//                                    "From next renewal, new subscription package price will be applicable.\n" +
//                                    "\n" +
//                                    "For more information you can send us a message. Use the \"contact us\"  button.\n" +
//                                    "\n" +
//                                    "Thank You.", context.getString(R.string.app_name), context);
//                            isopenAppForFistTime= 10;
//                        }
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.d("TimeReceive", "onResponseReceived: " + response);
            }
        }, "", "");
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {

    }
}
