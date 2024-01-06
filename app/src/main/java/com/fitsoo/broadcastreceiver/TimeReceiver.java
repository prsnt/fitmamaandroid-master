package com.fitsoo.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import com.fitsoo.R;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by xyz on 23/9/17.
 */

public class TimeReceiver extends BroadcastReceiver {

    CountDownTimer timer;

    @Override
    public void onReceive(final Context context, Intent intent) {
        timer = new CountDownTimer(FitsooUtils.SERVICE_CALL_TIME, FitsooUtils.SERVICE_CALL_TIME) {
            //        timer = new CountDownTimer(5000,5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //Check if app is in Front and then call the webservice and then recall the Broadcast receiver

                if (FitsooUtils.isAppInFront) {
                    if (FitsooPref.getUserId(context) != 0) {
                        addLogOfTime(context);
                    }

                    Intent intent = new Intent();
                    intent.setAction("com.timer.broadcast_receiver");
                    context.sendBroadcast(intent);
                } else {
                    FitsooUtils.APPInBack = true;
                }

                timer = null;
            }
        };
        timer.start();
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
                    FitsooPref.setPurchaseToken(context, object.getString("purchaseToken"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("TimeReceive", "onResponseReceived: " + response);
            }
        }, "", "");
    }
}
