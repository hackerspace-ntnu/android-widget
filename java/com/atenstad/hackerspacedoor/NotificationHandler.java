package com.atenstad.hackerspacedoor;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationHandler extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().containsKey("door")) {
            String status = remoteMessage.getData().get("door");
            if (status.equals("open") || status.equals("closed")) {
                String title;
                String text;
                int color;
                if (status.equals("closed")) {
                    title = "Hackerspace er stengt";
                    text = "Men slack er alltid åpent";
                    color = new Color().argb(255, 226, 94, 72);
                } else {
                    title = "Hackerspace er åpent";
                    text = "Velkommen inn";
                    color = new Color().argb(255, 89, 170, 71);
                }

                SharedPreferences prefs = getApplicationContext().getSharedPreferences(WidgetMainActivity.PREFS_NAME, 0);
                prefs.edit().putString("status", status).apply();
                Intent intent = new Intent(WidgetMainActivity.STATUS_UPDATE);
                getApplicationContext().sendBroadcast(intent);

                if (prefs.getBoolean("open_notification", true) && status.equals("open") || prefs.getBoolean("close_notification", true) && status.equals("closed")) {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.logo_small_green)
                                    .setContentTitle(title)
                                    .setContentText(text)
                                    .setColor(color);
                    if (!prefs.getBoolean("notification_silent", false)) {
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        mBuilder.setSound(alarmSound);
                    }
                    if (prefs.getBoolean("notification_vibration", true)) {
                        mBuilder.setVibrate(new long[] { 1000, 500, 1000, 500, 1000 });
                    }

                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(001, mBuilder.build());
                }
            }
        }
    }
}
