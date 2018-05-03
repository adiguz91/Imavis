package com.drone.imavis.mvp.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by adigu on 27.07.2017.
 */

public class FlyplanNotificationService extends IntentService {

    public FlyplanNotificationService() {
        this(FlyplanNotificationService.class.getName());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FlyplanNotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // TODO
        // 1. request in time interval (all 5min) for available tasks
        //      - save last login date in prefs, check if status of running tasks are completed
        // 2. create notifications -> click on notification will open the task/flyplan details
    }

    // R.drawable.cast_ic_notification_small_icon
    private void createNotification(int id, int iconRes, String title, String body) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(iconRes)
                        .setContentTitle(title)
                        .setContentText(body);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, notificationBuilder.build());
    }
}
