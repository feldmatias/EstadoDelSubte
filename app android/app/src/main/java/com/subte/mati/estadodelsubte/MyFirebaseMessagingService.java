package com.subte.mati.estadodelsubte;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String line = remoteMessage.getData().get("line");
            String state = remoteMessage.getData().get("state");
            state = Html.fromHtml(state, Html.FROM_HTML_MODE_LEGACY).toString();
            if (this.sendNotification(line)) {
                SubwayNotification notification = new SubwayNotification(this, line, state);
                notification.send();
            }
        }
    }

    private boolean sendNotification(String line) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean(line, false);
    }
}
