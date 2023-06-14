package com.wraith.wiregrard;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class VpnApplication extends Application {

    public static final String CHANNEL_ID = "rebotService";
    public static final String CHANNEL_NAME = "Auto Start Service Channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

    }
}
