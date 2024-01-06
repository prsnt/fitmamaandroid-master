package com.fitsoo.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fitsoo.R;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.fragment.MoreFragment;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.TermsAndPrivacy;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.fitsoo.activity.LoginActivity.isfromLogin;
import static com.fitsoo.activity.WelcomeActivity.IsfromLoginOrSignup;
import static com.fitsoo.utils.FitsooUtils.showMessageAlert;

public class SubscribeActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout relative, relative2;
    RelativeLayout relativeLeft2, relativeLeft;
    TextView txtSkip;
    ImageView imgShare;
    private ImageView imgHamburger;

    private ConsumeResponseListener listener;
    private BillingClient mBillingClient;

    int come_from = 0;
    private int type;
    private SkuDetailsParams skuDetailsParams;
    private List<SkuDetails> skuDetails;
    private List<String> skuList;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_offer);

        if (getIntent() != null) {
            if (getIntent().hasExtra("from")) {
                come_from = getIntent().getIntExtra("from", 0);
            }
        }

        relative = findViewById(R.id.relative);
        imgShare = (ImageView) findViewById(R.id.menu_share);
        relative2 = findViewById(R.id.relative2);
        relativeLeft = findViewById(R.id.relativeLeft);
        relativeLeft2 = findViewById(R.id.relativeLeft2);
        txtSkip = findViewById(R.id.txtSkip);
        relative.setOnClickListener(this);
        imgHamburger = (ImageView) findViewById(R.id.menu_hamburger);

        imgShare.setOnClickListener(this);

        relative2.setOnClickListener(this);

        if (IsfromLoginOrSignup) {
            imgHamburger.setVisibility(View.GONE);
            IsfromLoginOrSignup = false;
        } else {
            imgHamburger.setOnClickListener(this);
            imgHamburger.setVisibility(View.VISIBLE);
        }
        txtSkip.setOnClickListener(this);
        setUI();
        skuList = new ArrayList<>();
        skuList.add("com.fitmama.monthlyplan");
        skuList.add("com.fitmama.yearlyplan");
        setInitialBillingInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setUI() {
        switch (FitsooPref.getSubscribeType(this)) {
            case 1:
                type = 1;
                relative2.setBackground(getResources().getDrawable(R.drawable.unselect));
                relative.setBackground(getResources().getDrawable(R.drawable.select));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    relativeLeft.setBackgroundTintList(ContextCompat.getColorStateList(SubscribeActivity.this, R.color.pink));
                    relativeLeft2.setBackgroundTintList(ContextCompat.getColorStateList(SubscribeActivity.this, R.color.textcolor_dark));
                }
                relative.setEnabled(false);
                txtSkip.setText("Cancle Subscription");
                break;
            case 2:
                type = 2;
                relative2.setBackground(getResources().getDrawable(R.drawable.select));
                relative.setBackground(getResources().getDrawable(R.drawable.unselect));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    relativeLeft2.setBackgroundTintList(ContextCompat.getColorStateList(SubscribeActivity.this, R.color.pink));
                    relativeLeft.setBackgroundTintList(ContextCompat.getColorStateList(SubscribeActivity.this, R.color.textcolor_dark));
                }
                relative2.setEnabled(false);
                txtSkip.setText("Cancle Subscription");
                break;
            case 3:
                type = 3;
                relative2.setEnabled(true);
                relative.setEnabled(true);
                relative2.setBackground(getResources().getDrawable(R.drawable.unselect));
                relative.setBackground(getResources().getDrawable(R.drawable.unselect));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    relativeLeft.setBackgroundTintList(ContextCompat.getColorStateList(SubscribeActivity.this, R.color.textcolor_dark));
                    relativeLeft2.setBackgroundTintList(ContextCompat.getColorStateList(SubscribeActivity.this, R.color.textcolor_dark));
                }
                break;
            case 0:
                type = 0;
                relative2.setEnabled(true);
                relative.setEnabled(true);
                relative2.setBackground(getResources().getDrawable(R.drawable.unselect));
                relative.setBackground(getResources().getDrawable(R.drawable.unselect));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    relativeLeft.setBackgroundTintList(ContextCompat.getColorStateList(SubscribeActivity.this, R.color.textcolor_dark));
                    relativeLeft2.setBackgroundTintList(ContextCompat.getColorStateList(SubscribeActivity.this, R.color.textcolor_dark));
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.menu_share:
                shareMethod();
                break;
            case R.id.menu_hamburger:
                onBackPressed();
                break;
            case R.id.txtSkip:
                /*if (come_from == 0) {
                    Intent i = new Intent(SubscribeActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else*/;
                if (FitsooPref.getSubscribeType(this) == 1 || FitsooPref.getSubscribeType(this) == 2) {
                    String url = "https://play.google.com/store/account/subscriptions?sku=" + skuDetails.get(0).getSku() + "&package=" + getApplicationContext().getPackageName();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                   /* RequestQueue queue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    Log.d("SB-Response is:",response.substring(0,500));
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("SB-",error.toString());
                        }
                    });*/

// Add the request to the RequestQueue.
                    // queue.add(stringRequest);

                } else {
                    if (isfromLogin) {
                        isfromLogin = false;
                        startActivity(new Intent(SubscribeActivity.this, MainActivity.class));
                        finish();
                    } else {
                        onBackPressed();
//                        finish();
                    }
                }
                break;
            case R.id.relative2:
                try {
                    if(FitsooPref.getpackage_bought_from(this).equals("Android")|| FitsooPref.getpackage_bought_from(this).equals("")){
                        initiateSubscribe(2);
                    }else {
                        FitsooUtils.showMessageAlert("You can not upgrade/downgrade your existing package from here as you bought your package from "+FitsooPref.getpackage_bought_from(this) +"\n You can upgrade / downgrade package from the same device in which you bought your existing package", getString(R.string.app_name), this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.relative:
                try {
                    if(FitsooPref.getpackage_bought_from(this).equals("Android")|| FitsooPref.getpackage_bought_from(this).equals("")){
                        initiateSubscribe(1);
                    }else {
                        FitsooUtils.showMessageAlert("You can not upgrade/downgrade your existing package from here as you bought your package from "+FitsooPref.getpackage_bought_from(this) +"\n You can upgrade / downgrade package from the same device in which you bought your existing package", getString(R.string.app_name), this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void initiateSubscribe(int subscribefor) throws JSONException {
        switch (subscribefor) {
            case 1:
                BillingFlowParams flowParams;
                if (type == 2) {
                    flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetails.get(0))
                            .setOldSku(skuDetails.get(1).getSku(), FitsooPref.getPurchaseToken(this))
                            .setReplaceSkusProrationMode(BillingFlowParams.ProrationMode.IMMEDIATE_WITHOUT_PRORATION)
                            .build();
                } else {
                    flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetails.get(0))
                            .build();
                }
                mBillingClient.launchBillingFlow(SubscribeActivity.this, flowParams);
                break;

            case 2:
                if (type == 1) {
                    flowParams = BillingFlowParams.newBuilder()
                            .setOldSku(skuDetails.get(0).getSku(), FitsooPref.getPurchaseToken(this))
                            .setSkuDetails(skuDetails.get(1))
                            .setReplaceSkusProrationMode(BillingFlowParams.ProrationMode.IMMEDIATE_WITH_TIME_PRORATION)
                            .build();
                } else {
                    flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetails.get(1))
                            .build();
                }
                mBillingClient.launchBillingFlow(SubscribeActivity.this, flowParams);
                break;
        }
    }

    private void setInitialBillingInfo() {
        listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String s) {
                Log.e("sub", "onConsumeResponse: "+billingResult );
            }
        };

        mBillingClient = BillingClient.newBuilder(SubscribeActivity.this).enablePendingPurchases().setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> list) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                    for (final Purchase purchase : list) {
                        Log.e("SubscriptionActivity", "onPurchasesUpdated() returned: " + purchase.getOriginalJson());
                        //TODO call when consumed But to check for SUBSCRIPTION TYPE
//                        mBillingClient.consumeAsync("inapp:"+getPackageName()+":android.test.purchased" , listener);
                        SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd");
                        final String newDateStr = postFormater.format(Calendar.getInstance().getTime());

                        if (FitsooUtils.isInternetAvailable(SubscribeActivity.this)) {
                            JSONObject object = new JSONObject();
                            try {
                                object.put(FitsooConstant.REQ_USER_ID, FitsooPref.getUserId(SubscribeActivity.this));

                                JSONObject purchaseObject = new JSONObject(purchase.getOriginalJson());
                                if (purchaseObject.getString("productId").equalsIgnoreCase(getString(R.string.monthly_sub))) {
                                    object.put("subscription_type", "1");
                                } else {
                                    object.put("subscription_type", "2");
                                }
                                object.put("device_type", "Android");
                                object.put("receipt_data", purchase.getOriginalJson());
                                FitsooUtils.performRequest(SubscribeActivity.this, object.toString(), getString(R.string.BASE_URL) + getString(R.string.subscription_API), new OnReceiveResponseListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onResponseReceived(String requestType, String response) {
                                        FitsooPref.setString(SubscribeActivity.this, purchase.getOriginalJson());
                                        FitsooPref.setAccess(SubscribeActivity.this, 1);
                                        try {
                                            JSONObject purchaseObject = new JSONObject(purchase.getOriginalJson());
                                            if (purchaseObject.getString("productId").equalsIgnoreCase(getString(R.string.monthly_sub))) {
                                                FitsooPref.setSubscribeType(SubscribeActivity.this, 1);
                                                Log.e("SubscriptionActivity", "onResponseReceived: " + 1);
                                            } else {
                                                FitsooPref.setSubscribeType(SubscribeActivity.this, 2);
                                                Log.e("SubscriptionActivity", "onResponseReceived: " + 2);
                                            }
                                            FitsooPref.setPurchaseToken(SubscribeActivity.this, purchaseObject.getString("purchaseToken"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        setUI();
//                                        Toast.makeText(SubscribeActivity.this, "Response >>>" + response, Toast.LENGTH_SHORT).show();
                                    }
                                }, getString(R.string.str_please_wait), "");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        Toast.makeText(SubscribeActivity.this, "Purchase Successfull", Toast.LENGTH_SHORT).show();

                    }
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                    Toast.makeText(SubscribeActivity.this, "Purchase Cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SubscribeActivity.this, "Unknown Error Purchase", Toast.LENGTH_SHORT).show();
                }
            }
        }).build();

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The billing client is ready. You can query purchases here.

                    SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
                            .setSkusList(skuList).setType(BillingClient.SkuType.SUBS).build();
                    mBillingClient.querySkuDetailsAsync(skuDetailsParams,
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {

                                    skuDetails = list;
                                }
                            });
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });

        mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS,
                new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> list) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                && list != null) {
                            for (PurchaseHistoryRecord purchase : list) {
                                FitsooPref.setString(SubscribeActivity.this, purchase.getOriginalJson());
                                //Toast.makeText(util, ""+purchase.getOriginalJson(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    void shareMethod() {
        FitsooUtils.performRequest(SubscribeActivity.this, "", getString(R.string.BASE_URL) + getString(R.string.cmspage), new OnReceiveResponseListener() {
            @Override
            public void onResponseReceived(String requestType, String response) {
                Gson gson = new Gson();
                BaseResponse<TermsAndPrivacy> termsAndPrivacy = (gson).fromJson(response, new TypeToken<BaseResponse<TermsAndPrivacy>>() {
                }.getType());
                FitsooPref.setCMSresponse(SubscribeActivity.this, response);
                if (!FitsooUtils.hasValue(termsAndPrivacy.getData().getAndroid())) {
                    String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Check this new Fitness app, It's awesome!. \nTo check it out click the URL given below!\n" + url);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                } else {
                    String url = termsAndPrivacy.getData().getAndroid();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }
        }, getString(R.string.str_please_wait), "");
    }
}
