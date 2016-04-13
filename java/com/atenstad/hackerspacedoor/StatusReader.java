package com.atenstad.hackerspacedoor;

import android.content.Context;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StatusReader extends AsyncTask<String, String, String> {

    HttpURLConnection urlConnection;
    private WidgetMainActivity activity;
    private Context context;
    private boolean toastStatus;

    public StatusReader(WidgetMainActivity activity, Context context, Boolean toastStatus) {
        this.activity = activity;
        this.context = context;
        this.toastStatus = toastStatus;
    }

    @Override
    protected String doInBackground(String... args) {
        //ArrayList<Integer> data = new ArrayList();
        int value = -1;
        try {
            //https://xxxxxxxxxx.localtunnel.me/door/get_status/
            //https://beta.hackerspace-ntnu.no/door/get_status/
            URL url = new URL("https://hackerspace-ntnu.no/door/get_status/");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            value = reader.read();
            /*while (value != -1) {
                data.add(value);
                value = reader.read();
            }*/
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
        activity.handleStatus(result, context, toastStatus);
    }
}