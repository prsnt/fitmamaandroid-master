package com.fitsoo.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import com.fitsoo.R;
import com.fitsoo.activity.SplashActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by system  on 24/4/17.
 */

public class FitsooMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        showNotification(remoteMessage.getData());
    }

    // Checks if Application is running
    private boolean applicationInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
        boolean isActivityFound = false;

        if (services.get(0).processName.equalsIgnoreCase(getPackageName())) {
            isActivityFound = true;
        }

        return isActivityFound;
    }

    private void showNotification(Map<String, String> data) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Fitsoo")
                        .setAutoCancel(true)
                        .setContentText(data.get("body"));

        Intent notificationIntent = new Intent(this, SplashActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        mBuilder.setContentIntent(intent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());


//            NotificationCompat.Builder mBuilder =
//                    new NotificationCompat.Builder(this)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setContentTitle(data.get("title"))
//                            .setAutoCancel(true)
//                            .setContentText(data.get("body"));
//            JSONObject obj;
//            Bundle bundle = new Bundle();
//            try {
//                obj = new JSONObject(data.get("offerDetail"));
//                bundle.putString("blocId", String.valueOf(obj.getInt("bLocationId")));
//                bundle.putString("offerId", String.valueOf(obj.getInt("offerid")));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            NotificationManager mNotificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            mNotificationManager.notify(1, mBuilder.build());
    }
}