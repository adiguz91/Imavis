package com.drone.imavis.mvp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by adigu on 27.07.2017.
 */

public class DroneConnectionService extends IntentService {

    public DroneConnectionService() {
        this(DroneConnectionService.class.getName());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DroneConnectionService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // TODO
    }
}
