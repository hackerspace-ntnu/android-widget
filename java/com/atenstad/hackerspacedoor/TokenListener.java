package com.atenstad.hackerspacedoor;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class TokenListener extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        //String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("doorstatus");
        //FirebaseMessaging.getInstance().subscribeToTopic("debug");
    }
}
