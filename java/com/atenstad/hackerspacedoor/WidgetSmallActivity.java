package com.atenstad.hackerspacedoor;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

public class WidgetSmallActivity extends WidgetMainActivity {

    public int getDefaultImage() {
        return R.drawable.logo_small_blue;
    }

    public void onAppWidgetOptionsChanged (Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String status = prefs.getString("status", "error");

        updateImage(context);

        if (status.equals("error")) {
            this.statusReader = new StatusReader(this, context, false);
            this.statusReader.execute();
        }
    }

    public void updateImage(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget);

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String status = prefs.getString("status", "error");
        //Boolean bg = Boolean.parseBoolean(prefs.getString("bg", "false"));

        if (status.equals("error")) {
            views.setImageViewResource(R.id.imageView, R.drawable.logo_small_blue);
        } else if (status.equals("open")) {
            views.setImageViewResource(R.id.imageView, R.drawable.logo_small_green);
        } else if (status.equals("closed")) {
            views.setImageViewResource(R.id.imageView, R.drawable.logo_small_red);
        }

        ComponentName watchWidget = new ComponentName(context, WidgetSmallActivity.class );
        (AppWidgetManager.getInstance(context)).updateAppWidget(watchWidget, views);
    }
}