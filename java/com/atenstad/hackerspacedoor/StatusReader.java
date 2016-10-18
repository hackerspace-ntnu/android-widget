package com.atenstad.hackerspacedoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StatusReader extends AsyncTask<String, String, String> {

    HttpURLConnection urlConnection;
    Context context;

    public StatusReader(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
        int value = -1;
        try {
            URL url = new URL("https://hackerspace-ntnu.no/door/get_status/");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            value = reader.read();
        } catch (java.net.SocketTimeoutException e) {
            return "error";
        } catch (java.io.IOException e) {
            return "error";
        } finally {
            urlConnection.disconnect();
        }

        if (value == 84) {
            return "open";
        } else if (value == 70) {
            return "closed";
        }
        return "error";
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("closed")) {
            Toast.makeText(context, "Hackerspace er stengt", Toast.LENGTH_SHORT).show();
        } else if (result.equals("open")) {
            Toast.makeText(context, "Hackerspace er åpent", Toast.LENGTH_SHORT).show();
        } else if (result.equals("error")) {
            Toast.makeText(context, "Utilgjengelig status", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences prefs = context.getSharedPreferences(WidgetMainActivity.PREFS_NAME, 0);
        prefs.edit().putString("status", result).apply();

        Intent intent = new Intent(WidgetMainActivity.STATUS_UPDATE);
        context.sendBroadcast(intent);
    }
}