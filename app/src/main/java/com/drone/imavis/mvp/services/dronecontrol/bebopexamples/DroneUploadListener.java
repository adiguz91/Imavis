package com.drone.imavis.mvp.services.dronecontrol.bebopexamples;

/**
 * Created by adigu on 14.09.2017.
 */
/*
public class DroneUploadListener implements ARDataTransferUploaderProgressListener, ARDataTransferUploaderCompletionListener {


    private final ARFeatureCommon featureCommon;

    public DroneUploadListener(final ARFeatureCommon featureCommon) {
        this.featureCommon = featureCommon;
    }

    @Override
    public void didUploadComplete(Object arg, final ARDATATRANSFER_ERROR_ENUM error) {
        //if (ARPro3Application.DEBUG) Log.d(CLASS_NAME, "mavlink didUploadComplete status=" + error.name());

        final Object lock = new Object();

        synchronized (lock) {
            new Thread() {
                @Override
                public void run() {
                    synchronized (lock) {
                        uploader.cancelThread();
                        uploader.dispose();
                        uploader = null;

                        uploadManager.closeWifiFtp();
                        uploadManager.dispose();
                        uploadManager = null;

                        dataTransferManager.dispose();
                        dataTransferManager = null;

                        uploadHandlerThread.quit();
                        uploadHandlerThread = null;

                        //if (ARPro3Application.DEBUG) Log.d(CLASS_NAME, "released uploader resources");

                        if (featureCommon != null && error == ARDATATRANSFER_ERROR_ENUM.ARDATATRANSFER_OK) {
                            //if (ARPro3Application.DEBUG) Log.d(CLASS_NAME, "sendMavlinkStart");
                            featureCommon.sendMavlinkStart("flightPlan.mavlink", ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM.ARCOMMANDS_COMMON_MAVLINK_START_TYPE_FLIGHTPLAN);
                        }
                    }
                }
            }.start();
        }
    }

    @Override
    public void didUploadProgress(Object arg, float percent) {
        //if (ARPro3Application.DEBUG)
           // Log.d(CLASS_NAME, "mavlink file upload progress=" + percent);
    }



    public ARCONTROLLER_ERROR_ENUM prepareAutonomousFlight(ARDiscoveryDeviceService currentDeviceService){

        String productIP = ((ARDiscoveryDeviceNetService) (currentDeviceService.getDevice())).getIp();
        try {
            // FTP connection
            ARUtilsFtpConnection arUtilsFtpConnection = new ARUtilsFtpConnection();
            arUtilsFtpConnection.createFtpConnection(productIP, 61, ARUtilsFtpConnection.FTP_ANONYMOUS, "");

            // If already exist, overwrite it
            arUtilsFtpConnection.put(FTP_FILE_NAME, MY_PATH_NAME + MY_FILE_NAME, null, null, ARUTILS_FTP_RESUME_ENUM.eARUTILS_FTP_RESUME_UNKNOWN_ENUM_VALUE);

            //Log.d(TAG, String.format("Flight plan %s has been put on bebop FTP server", FTP_FILE_NAME));
        } catch (Exception ex) {
            //Log.w(TAG, String.format("AutonomousFlight prepare error : ", ex.getMessage()));
        }

        return sendstartMaVLink();
    }
}
*/