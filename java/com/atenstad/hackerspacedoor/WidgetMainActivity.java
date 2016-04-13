package com.atenstad.hackerspacedoor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

public abstract class WidgetMainActivity extends AppWidgetProvider {
    public static final String PREFS_NAME = "com.atenstad.hackerspacedoor";
    protected StatusReader statusReader;
    public static String BUTTON_CLICK = "ButtonClick";
    public WidgetMainActivity thisThing = this;

    public abstract int getDefaultImage();
    public abstract void updateImage(Context context);
    public abstract void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions);

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        for(int i=0; i<appWidgetIds.length; i++){
            int currentWidgetId = appWidgetIds[i];

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setAction(BUTTON_CLICK);

            statusReader = new StatusReader(this, context, false);

            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.activity_widget);
            views.setImageViewResource(R.id.imageView, getDefaultImage());
            views.setOnClickPendingIntent(R.id.imageView, getPendingSelfIntent(context, BUTTON_CLICK));

            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            String status = prefs.getString("status", "error");

            if (status.equals("error")) {
                this.statusReader = new StatusReader(this, context, false);
                this.statusReader.execute();
            } else {
                updateImage(context);
            }

            appWidgetManager.updateAppWidget(currentWidgetId, views);
        }
    }

    public void handleStatus(String status, Context context, Boolean toastStatus) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().putString("status", status).apply();

        updateImage(context);

        if (toastStatus) {
            if (status.equals("closed")) {
                Toast.makeText(context, "Hackerspace er stengt", Toast.LENGTH_SHORT).show();
            } else if (status.equals("open")) {
                Toast.makeText(context, "Hackerspace er åpent", Toast.LENGTH_SHORT).show();
            } else if (status.equals("error")) {
                Toast.makeText(context, "Utilgjengelig status", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (BUTTON_CLICK.equals(intent.getAction()) || intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            statusReader = new StatusReader(this, context, BUTTON_CLICK.equals(intent.getAction()));
            statusReader.execute();
        }
        /*if (intent.getAction().equals("UpdateBg")) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            prefs.edit().putString("bg", intent.getBooleanExtra("bg", false)+"").apply();
            updateImage(context);
        }*/
    }
}
