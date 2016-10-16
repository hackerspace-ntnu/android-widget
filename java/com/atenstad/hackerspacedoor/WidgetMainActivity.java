package com.atenstad.hackerspacedoor;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetMainActivity extends AppWidgetProvider {
    public static final String PREFS_NAME = "com.atenstad.hackerspacedoor";
    public static final String STATUS_UPDATE = "com.atenstad.hackerspacedoor.STATUS_UPDATE";
    public static final String BUTTON_CLICK = "com.atenstad.hackerspacedoor.BUTTON_CLICK";

    int unknownImage, openImage, closedImage;

    public WidgetMainActivity(int unknownImage, int openImage, int closedImage) {
        this.unknownImage = unknownImage;
        this.openImage = openImage;
        this.closedImage = closedImage;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            int currentWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget);

            Intent intent = new Intent(context, getClass());
            intent.setAction(BUTTON_CLICK);
            views.setOnClickPendingIntent(R.id.imageView, PendingIntent.getBroadcast(context, 0, intent, 0));

            views.setImageViewResource(R.id.imageView, unknownImage);
            appWidgetManager.updateAppWidget(currentWidgetId, views);

            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            String status = prefs.getString("status", "error");

            if (status.equals("error")) {
                new StatusReader(context).execute();
            } else {
                onReceive(context, new Intent(STATUS_UPDATE));
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (BUTTON_CLICK.equals(intent.getAction())) {
            new StatusReader(context).execute();
        }
        if (STATUS_UPDATE.equals(intent.getAction())) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            String status = prefs.getString("status", "error");

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget);

            if (status.equals("open")) {
                views.setImageViewResource(R.id.imageView, openImage);
            } else if (status.equals("closed")) {
                views.setImageViewResource(R.id.imageView, closedImage);
            } else {
                views.setImageViewResource(R.id.imageView, unknownImage);
            }

            ComponentName watchWidget = new ComponentName(context, getClass());
            (AppWidgetManager.getInstance(context)).updateAppWidget(watchWidget, views);
        }
    }
}