package com.drone.imavis.mvp.services.dronecontrol;

import android.util.Log;

import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM;
import com.parrot.arsdk.arcontroller.ARFeatureCommon;
import com.parrot.arsdk.ardatatransfer.ARDATATRANSFER_ERROR_ENUM;
import com.parrot.arsdk.ardatatransfer.ARDataTransferUploader;
import com.parrot.arsdk.ardatatransfer.ARDataTransferUploaderCompletionListener;
import com.parrot.arsdk.ardatatransfer.ARDataTransferUploaderProgressListener;

/**
 * Created by adigu on 14.09.2017.
 */

class UploadListener implements ARDataTransferUploaderProgressListener, ARDataTransferUploaderCompletionListener {

    private final ARFeatureCommon featureCommon;
    private ARDataTransferUploader uploader;

    public UploadListener(final ARFeatureCommon featureCommon, ARDataTransferUploader uploader) {
        this.featureCommon = featureCommon;
        this.uploader = uploader;
    }

    @Override
    public void didUploadComplete(Object arg, final ARDATATRANSFER_ERROR_ENUM error) {
        //if (ARPro3Application.DEBUG) Log.d(TAG, "mavlink didUploadComplete status=" + error.name());

        final Object lock = new Object();

        synchronized (lock) {
            new Thread() {
                @Override
                public void run() {
                    synchronized (lock) {
                        //uploader.cancelThread();
                        //uploader.dispose();
                        //uploader = null;

                        //uploadManager.closeWifiFtp();
                        //uploadManager.dispose();
                        //uploadManager = null;

                        //dataTransferManager.dispose();
                        //dataTransferManager = null;

                        //uploadHandlerThread.quit();
                        //uploadHandlerThread = null;

                        //if (ARPro3Application.DEBUG) Log.d(TAG, "released uploader resources");

                        if (featureCommon != null && error == ARDATATRANSFER_ERROR_ENUM.ARDATATRANSFER_OK) {
                            // if (ARPro3Application.DEBUG) Log.d(TAG, "sendMavlinkStart");
                            featureCommon.sendMavlinkStart("flightPlan.mavlink", ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM.ARCOMMANDS_COMMON_MAVLINK_START_TYPE_FLIGHTPLAN);
                            Log.d("sendMavlinkStart","SendMavlink is Success");
                        }
                    }
                }
            }.start();
        }
    }

    @Override
    public void didUploadProgress(Object arg, float percent) {
            Log.d("sendMavlinkStart", "mavlink file upload progress=" + percent);
    }
}

/*
 * START A FLIGHTPLAN
    Start a FlightPlan:
    deviceController.getFeatureCommon().sendMavlinkStart((String)filepath, (ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM)type);
    Start a FlightPlan based on a mavlink file existing on the drone.

    Requirements are:
    * Product is calibrated
    * Product should be in outdoor mode
    * Product has fixed its GPS


    filepath (string): flight plan file path from the mavlink ftp root
    type (enum): type of the played mavlink file
    flightPlan: Mavlink file for FlightPlan
    mapMyHouse: Mavlink file for MapMyHouse
    Result:
    If the FlightPlan has been started, event FlightPlanPlayingStateChanged is triggered with param state set to playing.
    Otherwise, event FlightPlanPlayingStateChanged is triggered with param state set to stopped and event MavlinkPlayErrorStateChanged is triggered with an explanation of the error.
 */