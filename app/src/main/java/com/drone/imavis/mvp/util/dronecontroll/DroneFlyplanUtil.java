package com.drone.imavis.mvp.util.dronecontroll;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;

import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM;
import com.parrot.arsdk.arcontroller.ARFeatureCommon;
import com.parrot.arsdk.ardatatransfer.ARDATATRANSFER_UPLOADER_RESUME_ENUM;
import com.parrot.arsdk.ardatatransfer.ARDataTransferManager;
import com.parrot.arsdk.ardatatransfer.ARDataTransferUploader;
import com.parrot.arsdk.ardiscovery.ARDISCOVERY_PRODUCT_ENUM;
import com.parrot.arsdk.ardiscovery.UsbAccessoryMux;
import com.parrot.arsdk.armavlink.ARMavlinkException;
import com.parrot.arsdk.armavlink.ARMavlinkFileGenerator;
import com.parrot.arsdk.armavlink.ARMavlinkMissionItem;
import com.parrot.arsdk.arutils.ARUtilsManager;

import java.io.File;

/**
 * Created by adigu on 14.09.2017.
 */


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
public class DroneFlyplanUtil {

    /*
    public static String MAVLINK_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/mavlink_files";

    private ARDataTransferManager dataTransferManager;
    private ARUtilsManager uploadManager;

    public DroneFlyplanUtil() {}

    public void startFlying(Object flyplanData) {
        File flyplanFile = CreateFlyplanFile(flyplanData);

        // TODO
        // upload file to drone via ftp
        // send fly command
    }

    private File CreateFlyplanFile(Object flyplanData) {

        // add waypoints

        // add ROI's

        return null;
    }

    public String generateMavlinkFile()
    {
        final ARMavlinkFileGenerator generator;

        try {
            generator = new ARMavlinkFileGenerator();
        } catch (ARMavlinkException e) {
            //Log.e(CLASS_NAME, "generateMavlinkFile: " + e.getMessage(), e);
            return "";
        }

        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkDelay(10));
        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkTakeoffMissionItem(lat,lng, alt,(float)1.5, 0));
        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkNavWaypointMissionItem((float)35.0093, (float)-101.595,(float)1.5, 0));
        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkNavWaypointMissionItem((float)35.0097,(float)-101.592,(float)1.5, 0));
        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkLandMissionItem(lat,lng, 0, 0));

        // save our mavlink file
        final File file = new File(MAVLINK_STORAGE_DIRECTORY);
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();

        final String filename = MAVLINK_STORAGE_DIRECTORY + File.separator + "flightPlan.mavlink";
        final File mavFile = new File(filename);

        //noinspection ResultOfMethodCallIgnored
        mavFile.delete();
        generator.CreateMavlinkFile(filename);

        return filename;
    }

    public void autoFlight(String filename) {
        deviceController.getFeatureCommon().sendMavlinkStart(filename, ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM.ARCOMMANDS_COMMON_MAVLINK_START_TYPE_FLIGHTPLAN);
        //Log.d(TAG,"sending autoFlight Mavlink file");
    }

    public void transmitMavlinkFile(final Context ctx, final ARFeatureCommon featureCommon, final ARDISCOVERY_PRODUCT_ENUM product, final String address, final String filename) {
        try {
            dataTransferManager = new ARDataTransferManager();
            ARDataTransferUploader uploader = dataTransferManager.getARDataTransferUploader();
            uploadManager = new ARUtilsManager();

            if (product == ARDISCOVERY_PRODUCT_ENUM.ARDISCOVERY_PRODUCT_SKYCONTROLLER_2) {
                uploadManager.initWifiFtp(UsbAccessoryMux.get(ctx).getMux().newMuxRef(), address, 61, "", "");
            } else {
                uploadManager.initWifiFtp(address, 61, "", "");
            }

            final DroneUploadListener listener = new DroneUploadListener(featureCommon);
            uploader.createUploader(uploadManager, "flightPlan.mavlink", filename, listener, null, listener, null, ARDATATRANSFER_UPLOADER_RESUME_ENUM.ARDATATRANSFER_UPLOADER_RESUME_FALSE);

            HandlerThread uploadHandlerThread = new HandlerThread("mavlink_uploader");
            uploadHandlerThread.start();

            Runnable uploadRunnable = uploader.getUploaderRunnable();
            Handler uploadHandler = new Handler(uploadHandlerThread.getLooper());

            uploadHandler.post(uploadRunnable);

        } catch (Exception e) {
            //Log.e(CLASS_NAME, "transmitMavlinkFile exception: " + e.getMessage(), e);
        }
    }

    private void setROI() {
        final ARMavlinkMissionItem roi = ARMavlinkMissionItem.CreateMavlinkSetROI(MAV_ROI.MAV_ROI_LOCATION, 0, marker.getId(), (float) marker.getLatitude(), (float) marker.getLongitude(), 2);
        generator.addMissionItem(roi);

        final ARMavlinkMissionItem speed = ARMavlinkMissionItem.CreateMavlinkChangeSpeedMissionItem(1, marker.getSpeed(), 100);
        generator.addMissionItem(speed);

        view = ARMavlinkMissionItem.CreateMavlinkSetViewMode(MAV_VIEW_MODE_TYPE.VIEW_MODE_TYPE_ROI, marker.getPoi().getId());
        generator.addMissionItem(view);

        final ARMavlinkMissionItem destination = ARMavlinkMissionItem.CreateMavlinkNavWaypointMissionItem((float) marker.getLatitude(), (float) marker.getLongitude(), (float) marker.getAltitude(), (float) yaw);
        generator.addMissionItem(destination);
    }
    */
}
