package com.fitsoo.activity.baseclass;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.activity.SplashActivity;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


/**
 * Created by xyz on 18/7/17.
 */

public class BaseActivity extends AppCompatActivity {
    public static int isopenAppForFistTime = 0;
    public static int isopenAppForFistTimeForProfile = 0;
    public Handler handler;
    public Runnable r;

    @Override
    protected void onResume() {
        super.onResume();
        FitsooUtils.isAppInFront = true;
        if (FitsooUtils.APPInBack) {
            FitsooUtils.APPInBack = false;
            Intent intent = new Intent();
            intent.setAction("com.timer.broadcast_receiver");
            sendBroadcast(intent);
        }
        handler = new Handler();
        handler.postDelayed(
                r = new Runnable() {
                    @Override
                    public void run() {
//                        Log.e("TimeReceive ", "run: ");
                        if (handler != null) {
                            handler.postDelayed(r, 10000);
                        }
                        if (FitsooPref.getUserId(BaseActivity.this) != 0) {

                            addLogOfTime(BaseActivity.this);
                        }

                    }
                }, 10000);

//handler.post(r);

    }


    @Override
    protected void onPause() {
        super.onPause();
        FitsooUtils.isAppInFront = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isopenAppForFistTime = 0;
        isopenAppForFistTimeForProfile = 0;

        handler.removeCallbacksAndMessages(null);
    }



    private void addLogOfTime(final Context context) {

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
//                            FitsooUtils.showMessageAlertRed(getString(R.string.NOTICE), BaseActivity.this);
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
}
