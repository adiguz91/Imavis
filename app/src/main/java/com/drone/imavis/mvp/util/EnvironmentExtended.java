package com.drone.imavis.mvp.util;

import android.os.Environment;

/**
 * Created by adigu on 14.09.2017.
 */

public class EnvironmentExtended extends Environment {
    public static final String MAVLINK_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/mavlink_files";
}
