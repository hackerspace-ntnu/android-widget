package com.atenstad.hackerspacedoor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class AppSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        Switch switch_open_notification = (Switch) findViewById(R.id.switch_open_notification);
        Switch switch_close_notification = (Switch) findViewById(R.id.switch_close_notification);
        Switch switch_notification_silent = (Switch) findViewById(R.id.switch_notification_silent);
        Switch switch_notification_vibration = (Switch) findViewById(R.id.switch_notification_vibration);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(WidgetMainActivity.PREFS_NAME, 0);
        switch_open_notification.setChecked(prefs.getBoolean("open_notification", true));
        switch_close_notification.setChecked(prefs.getBoolean("close_notification", true));
        switch_notification_silent.setChecked(prefs.getBoolean("notification_silent", false));
        switch_notification_vibration.setChecked(prefs.getBoolean("notification_vibration", true));

        switch_open_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences prefs = getApplicationContext().getSharedPreferences(WidgetMainActivity.PREFS_NAME, 0);
                prefs.edit().putBoolean("open_notification", isChecked).apply();

            }
        });

        switch_close_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences prefs = getApplicationContext().getSharedPreferences(WidgetMainActivity.PREFS_NAME, 0);
                prefs.edit().putBoolean("close_notification", isChecked).apply();

            }
        });

        switch_notification_silent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences prefs = getApplicationContext().getSharedPreferences(WidgetMainActivity.PREFS_NAME, 0);
                prefs.edit().putBoolean("notification_silent", isChecked).apply();

            }
        });

        switch_notification_vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences prefs = getApplicationContext().getSharedPreferences(WidgetMainActivity.PREFS_NAME, 0);
                prefs.edit().putBoolean("notification_vibration", isChecked).apply();

            }
        });
    }

}
