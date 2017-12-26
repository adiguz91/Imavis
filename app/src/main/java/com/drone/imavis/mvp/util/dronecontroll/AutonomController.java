package com.drone.imavis.mvp.util.dronecontroll;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.dronecontrol.SDCardModule;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.google.android.gms.maps.model.LatLng;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEOV2_RECORD_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIASTREAMING_VIDEOSTREAMMODE_MODE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_AUTOWHITEBALANCESELECTION_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_PICTUREFORMATSELECTION_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEOFRAMERATE_FRAMERATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEORESOLUTIONS_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_WIFI_SECURITY_KEY_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_ERROR_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerCodec;
import com.parrot.arsdk.arcontroller.ARControllerException;
import com.parrot.arsdk.arcontroller.ARDeviceController;
import com.parrot.arsdk.arcontroller.ARDeviceControllerStreamListener;
import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardatatransfer.ARDATATRANSFER_UPLOADER_RESUME_ENUM;
import com.parrot.arsdk.ardatatransfer.ARDataTransferException;
import com.parrot.arsdk.ardatatransfer.ARDataTransferManager;
import com.parrot.arsdk.ardatatransfer.ARDataTransferUploader;
import com.parrot.arsdk.ardiscovery.ARDISCOVERY_PRODUCT_ENUM;
import com.parrot.arsdk.ardiscovery.ARDISCOVERY_PRODUCT_FAMILY_ENUM;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDevice;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceNetService;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;
import com.parrot.arsdk.ardiscovery.ARDiscoveryException;
import com.parrot.arsdk.ardiscovery.ARDiscoveryService;
import com.parrot.arsdk.armavlink.ARMavlinkException;
import com.parrot.arsdk.armavlink.ARMavlinkFileGenerator;
import com.parrot.arsdk.armavlink.ARMavlinkMissionItem;
import com.parrot.arsdk.arutils.ARUtilsException;
import com.parrot.arsdk.arutils.ARUtilsManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adigu on 14.09.2017.
 */

// http://developer.parrot.com/docs/reference/bebop_2/index.html
public class AutonomController implements IAutonomousFlightController {

    // constants
    public static final String DATE_FORMAT_ISO_8601 = "yyyy-mm-dd";
    public static final String TIME_FORMAT_ISO_8601 = "hh:mm:ss";
    public static final String MAVLINK_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/mavlink_files";
    private static final String TAG = "BebopDrone";
    private static final int FTP_FLIGHTPLAN = 61; //21

    private final List<AutonomousFlightControllerListener> mListeners;
    private final Handler mHandler;
    private SDCardModule mSDCardModule;
    private ARUtilsManager mFtpUploadManager;
    private ARUtilsManager mFtpQueueManager;

    private ARDeviceController mDeviceController;
    private ARCONTROLLER_DEVICE_STATE_ENUM mState;
    private ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM mFlyingState;
    private String mCurrentRunId;
    private ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM mAutoFlyingState;
    private ARDiscoveryDeviceService mDeviceService;

    private int mBatteryStatus;
    private String mRunId;
    private boolean mCalibrationIsRequired;
    private DroneDeviceControllerListener droneDeviceController;

    public AutonomController(Context context, @NonNull ARDiscoveryDeviceService deviceService) throws ARUtilsException {

        mFtpUploadManager = new ARUtilsManager();
        mFtpQueueManager = new ARUtilsManager();
        mDeviceService = deviceService;
        mListeners = new ArrayList<>();
        // needed because some callbacks will be called on the main thread
        mHandler = new Handler(context.getMainLooper());
        mState = ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED;

        mSDCardModule = new SDCardModule(mFtpUploadManager, mFtpQueueManager);
        droneDeviceController = new DroneDeviceControllerListener(context, mSDCardModule);

        // if the product type of the deviceService match with the types supported
        ARDISCOVERY_PRODUCT_ENUM productType = ARDiscoveryService.getProductFromProductID(deviceService.getProductID());
        ARDISCOVERY_PRODUCT_FAMILY_ENUM family = ARDiscoveryService.getProductFamily(productType);
        if (ARDISCOVERY_PRODUCT_FAMILY_ENUM.ARDISCOVERY_PRODUCT_FAMILY_ARDRONE.equals(family)) {
            ARDiscoveryDevice discoveryDevice = createDiscoveryDevice(deviceService, productType);
            if (discoveryDevice != null) {
                mDeviceController = createDeviceController(discoveryDevice);
                discoveryDevice.dispose();
            }
        }
        else {
            Log.e(TAG, "DeviceService type is not supported by BebopDrone");
        }
    }

    public void dispose()
    {
        if (mDeviceController != null)
            mDeviceController.dispose();
    }

    public void addListener(AutonomousFlightControllerListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(AutonomousFlightControllerListener listener) {
        mListeners.remove(listener);
    }

    public boolean connect() {
        boolean success = false;
        if ((mDeviceController != null) && (ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED.equals(mState))) {
            ARCONTROLLER_ERROR_ENUM error = mDeviceController.start();
            if (error == ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK) {
                success = true;
            }
        }
        return success;
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#reboot
     */
    public void reboot() {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureCommon().sendCommonReboot();
    }

    public void uploadAutonomousFlightPlan(FlyPlan flyPlan, String localFilepath) {
        try {
            String productIP = ((ARDiscoveryDeviceNetService)(mDeviceService.getDevice())).getIp();

            mFtpUploadManager.initWifiFtp(productIP, FTP_FLIGHTPLAN, "", "");
            //ftpUploadManager.initWifiFtp(productIP, FTP_FLIGHTPLAN, ARUtilsManager.FTP_ANONYMOUS, "");
            //ftpQueueManager.initWifiFtp(productIP, FTP_FLIGHTPLAN, ARUtilsManager.FTP_ANONYMOUS, "");

            ARDataTransferManager dataTransferManager = new ARDataTransferManager();
            ARDataTransferUploader uploader = dataTransferManager.getARDataTransferUploader();
            mFtpUploadManager = new ARUtilsManager();

            final UploadListener listener = new UploadListener(null, uploader);
            uploader.createUploader(mFtpUploadManager, "flightPlan.mavlink", localFilepath, listener, null, listener, null, ARDATATRANSFER_UPLOADER_RESUME_ENUM.ARDATATRANSFER_UPLOADER_RESUME_FALSE);

        } catch (ARUtilsException e) {
            e.printStackTrace();
        } catch (ARDataTransferException e) {
            e.printStackTrace();
        }
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#controller-gps-info
     * @param location
     * @param horizontalAccuracy
     * @param verticalAccuracy
     * @param northSpeed
     * @param eastSpeed
     * @param downSpeed
     * @param timestamp
     */
    public void changeHomeLocation(GPSCoordinate location, float horizontalAccuracy, float verticalAccuracy, float northSpeed, float eastSpeed, float downSpeed, double timestamp) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureControllerInfo().sendGps(location.getLatitude(), location.getLongitude(), (float)location.getAltitude(), horizontalAccuracy, verticalAccuracy, northSpeed, eastSpeed, downSpeed, timestamp);
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#set-max-distance
     * @param maxDistance
     */
    public void setMaxDistance(float maxDistance) {
        //mDeviceController.getFeatureARDrone3().sendPilotingSettingsNoFlyOverMaxDistance((byte)shouldNotFlyOver);
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendPilotingSettingsMaxDistance(maxDistance);
        Log.d("ARCONTROLLER_ERROR_ENUM", error.toString());
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#cancel-the-moveto
     */
    public void cancelMoveTo() {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendPilotingCancelMoveTo();
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#set-picture-format
     * @param type
     */
    public void setPictureFormat(ARCOMMANDS_ARDRONE3_PICTURESETTINGS_PICTUREFORMATSELECTION_TYPE_ENUM type) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendPictureSettingsPictureFormatSelection(type);
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#set-white-balance-mode
     * @param type
     */
    private void setWitheBalanceMode(ARCOMMANDS_ARDRONE3_PICTURESETTINGS_AUTOWHITEBALANCESELECTION_TYPE_ENUM type) {
        // auto
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendPictureSettingsAutoWhiteBalanceSelection(type);
    }

    /**
     * if type == 0 == video else image
     * http://developer.parrot.com/docs/reference/bebop_2/#record-a-video
     * @param record
     * @param type
     * @param interval
     */
    public void recordVideoOrTakePictures(ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEOV2_RECORD_ENUM record, int type, float interval) {
        if (type > 0)
            setPictureInterval(true, interval);
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendMediaRecordVideoV2(record); // start or stop
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#enable-disable-video-streaming
     * @param enable
     */
    public void enableVideoStreaming(boolean enable) {
        // SET THE ELECTRIC FREQUENCY
        // SET THE ANTIFLICKERING MODE
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendMediaStreamingVideoEnable((byte)(enable ? 1:0));
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#set-timelapse-mode
     * @param enable
     * @param interval
     */
    public void setPictureInterval(boolean enable, float interval) {
        // bool enabled
        // Once it is configured, you can start/stop the timelapse with the RecordVideo command.
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendPictureSettingsTimelapseSelection((byte)(enable ? 1:0), interval);
    }

    /**
     * Set the country for WiFi frequency band
     * Country code with ISO 3166 format
     * http://developer.parrot.com/docs/reference/bebop_2/#set-the-country
     * @param countryCode
     */
    public void setWifiSettingsCountry(String countryCode) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureCommon().sendSettingsCountry(countryCode);
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#enable-auto-country
     * @param automatic
     */
    public void setWifiSettingsAutoCountry(boolean automatic) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureCommon().sendSettingsAutoCountry((byte)(automatic ? 1:0));
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#set-the-wifi-security
     * @param type
     * @param key
     * @param keyType
     */
    public void setWifiSecurity(ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM type, String key, ARCOMMANDS_WIFI_SECURITY_KEY_TYPE_ENUM keyType) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureWifi().sendSetSecurity(type, key, keyType);
    }

    /**
     * DateTime with ISO-8601 format
     * http://developer.parrot.com/docs/reference/bebop_2/#set-the-date
     * http://developer.parrot.com/docs/reference/bebop_2/#set-the-time
     * @param dateTime
     */
    public void setCurrentDateTime(Date dateTime) {
        String date = new SimpleDateFormat(DATE_FORMAT_ISO_8601).format(dateTime);
        String time = new SimpleDateFormat(TIME_FORMAT_ISO_8601).format(dateTime);

        // will be used to save it to the picture - NOT SEND WHEN USING libARController
        ARCONTROLLER_ERROR_ENUM error1 = mDeviceController.getFeatureCommon().sendCommonCurrentDate(date);
        ARCONTROLLER_ERROR_ENUM error2 = mDeviceController.getFeatureCommon().sendCommonCurrentTime(time);
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#set-video-resolutions
     * http://developer.parrot.com/docs/reference/bebop_2/#set-video-framerate
     * @param type
     * @param frameRate
     */
    public void setPictureAndVideoSettings(ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEORESOLUTIONS_TYPE_ENUM type,
                                     ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEOFRAMERATE_FRAMERATE_ENUM frameRate) {
        ARCONTROLLER_ERROR_ENUM error1 = mDeviceController.getFeatureARDrone3().sendPictureSettingsVideoResolutions(type);
        ARCONTROLLER_ERROR_ENUM error2 = mDeviceController.getFeatureARDrone3().sendPictureSettingsVideoFramerate(frameRate);
    }

    /**
     * UNKNOWN TRIGGER
     * http://developer.parrot.com/docs/reference/bebop_2/#set-the-stream-mode
     * @param mode
     */
    public void setStreamingMode(ARCOMMANDS_ARDRONE3_MEDIASTREAMING_VIDEOSTREAMMODE_MODE_ENUM mode) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendMediaStreamingVideoStreamMode(mode);
    }

    /**
     * Starts on autonomous flight
     * http://developer.parrot.com/docs/reference/bebop_2/#set-video-autorecord-mode
     * @param enableAutoRecord
     * @param autoRecordMassStorageId
     */
    public void enableAutoVideoRecord(boolean enableAutoRecord, byte autoRecordMassStorageId) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendPictureSettingsVideoAutorecordSelection((byte)(enableAutoRecord ? 1:0), autoRecordMassStorageId);
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#set-the-return-home-delay
     * @param delayInSecond
     */
    public void setReturnHomeDelayAfterDisconnect(short delayInSecond) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendGPSSettingsReturnHomeDelay(delayInSecond);
    }

    /**
     * Used for gps fix,
     * http://developer.parrot.com/docs/reference/bebop_2/#set-controller-gps-location
     * @param homeCoordinate
     */
    public void setHomeLocation(GPSCoordinate homeCoordinate) {
        double horrizontalAccurancy = -1;
        double verticalAccurancy = -1;
        // TODO: altitude
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3()
                                            .sendGPSSettingsSendControllerGPS(homeCoordinate.getLatitude(), homeCoordinate.getLongitude(), homeCoordinate.getAltitude(),
                                                                              horrizontalAccurancy, verticalAccurancy);
        Log.d("ARCONTROLLER_ERROR_ENUM", error.toString());
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#set-the-preferred-home-type
     * @param type
     */
    public void setHomeType(ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM type) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendGPSSettingsHomeType(type);
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#set-max-altitude
     * @param maxAltitude
     */
    public void setMaxAltitude(float maxAltitude) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendPilotingSettingsMaxAltitude(maxAltitude);
    }

    public boolean disconnect() {
        boolean success = false;
        if ((mDeviceController != null) && (ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING.equals(mState))) {
            ARCONTROLLER_ERROR_ENUM error = mDeviceController.stop();
            if (error == ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK) {
                success = true;
            }
        }
        return success;
    }

    public ARCONTROLLER_DEVICE_STATE_ENUM getConnectionState() {
        return mState;
    }

    public ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM getFlyingState() {
        return mFlyingState;
    }

    public ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM getFlyPlanState() {
        return mAutoFlyingState;
    }

    public boolean isCalibrationRequired() {
        return mCalibrationIsRequired;
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#pause-a-flightplan
     */
    public void pauseAutonomousFlight() {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureCommon().sendMavlinkPause();
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#stop-a-flightplan
     */
    public void stopAutonomousFlight() {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureCommon().sendMavlinkStop();
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#set-returnhome-behavior-during-flightplan
     * @param enable
     */
    public void setReturnHomeOnDisconnect(boolean enable) {
        // delay have to been set
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureCommon().sendFlightPlanSettingsReturnHomeOnDisconnect((byte)(enable?1:0));
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#start-abort-magnetometer-calibration
     * @param start
     */
    public void calibration(boolean start) {
        ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureCommon().sendCalibrationMagnetoCalibration((byte)(start?1:0));
    }

    public String generateMavlinkFile(LatLng position, float altitude, float yaw) {

        final ARMavlinkFileGenerator generator;

        try {
            generator = new ARMavlinkFileGenerator();
        } catch (ARMavlinkException e) {
            //Log.e(Tag, "generateMavlinkFile: " + e.getMessage(), e);
            return "";
        }

        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkDelay(10));
        // Take off coordinates
        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkTakeoffMissionItem((float) position.latitude, (float) position.longitude, altitude, yaw, 0));
        // Way points - coordinates to travel
        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkNavWaypointMissionItem((float) position.latitude + 0.00002000f, (float) position.longitude + 0.00002000f, altitude, yaw));
        // Landing coordinates
        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkLandMissionItem((float) position.latitude,(float) position.longitude, 0, 0));
        //lat,long,alt,yaw

        // save our mavlink file
        //final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.MAVLINK_STORAGE_DIRECTORY));
        final File file = new File(MAVLINK_STORAGE_DIRECTORY);
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();

        final String filename = MAVLINK_STORAGE_DIRECTORY + File.separator + "flightPlan.mavlink";
        final File mavFile = new File(filename);

        //noinspection ResultOfMethodCallIgnored
        //mavFile.delete();
        generator.CreateMavlinkFile(filename);

        return filename;
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#start-a-flightplan
     */
    public void startAutonomousFlight(){
        String filename = "";
        mDeviceController.getFeatureCommon().sendMavlinkStart(filename, ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM.ARCOMMANDS_COMMON_MAVLINK_START_TYPE_FLIGHTPLAN);
        Log.d(TAG,"sending autoFlight Mavlink file");
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#land
     */
    public void land() {
        if ((mDeviceController != null) && (mState.equals(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING))) {
            ARCONTROLLER_ERROR_ENUM error = mDeviceController.getFeatureARDrone3().sendPilotingLanding();
        }
    }

    /**
     * http://developer.parrot.com/docs/reference/bebop_2/#return-home
     */
    public void returnHome(){
        if ((mDeviceController != null) || (mState.equals(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING))) {
            ARCONTROLLER_ERROR_ENUM error =  mDeviceController.getFeatureARDrone3().sendPilotingNavigateHome((byte) 1);
        }
    }

    public void getLastFlightMedias() {
        String runId = mCurrentRunId;
        if ((runId != null) && !runId.isEmpty()) {
            mSDCardModule.getFlightMedias(runId);
        } else {
            //Log.e(TAG, "RunID not available, fallback to the day's medias");
            mSDCardModule.getTodaysFlightMedias();
        }
    }

    public int getBatteryStatus() {
        return mBatteryStatus;
    }

    public String getRunId() {
        return mRunId;
    }

    public void cancelGetLastFlightMedias() {
        mSDCardModule.cancelGetFlightMedias();
    }

    private ARDiscoveryDevice createDiscoveryDevice(@NonNull ARDiscoveryDeviceService service, ARDISCOVERY_PRODUCT_ENUM productType) {
        ARDiscoveryDevice device = null;
        try {
            device = new ARDiscoveryDevice();

            ARDiscoveryDeviceNetService netDeviceService = (ARDiscoveryDeviceNetService) service.getDevice();
            device.initWifi(productType, netDeviceService.getName(), netDeviceService.getIp(), netDeviceService.getPort());

        } catch (ARDiscoveryException e) {
            Log.e(TAG, "Exception", e);
            Log.e(TAG, "Error: " + e.getError());
        }

        return device;
    }

    private ARDeviceController createDeviceController(@NonNull ARDiscoveryDevice discoveryDevice) {
        ARDeviceController deviceController = null;
        try {
            deviceController = new ARDeviceController(discoveryDevice);

            deviceController.addListener(droneDeviceController.getDeviceControllerListener());
            deviceController.addStreamListener(mStreamListener);
        } catch (ARControllerException e) {
            //Log.e(TAG, "Exception", e);
        }

        return deviceController;
    }

    //region notify listener block
    private void notifyConnectionChanged(ARCONTROLLER_DEVICE_STATE_ENUM state) {
        /*List<AutonomousFlightControllerListener> listenersCpy = new ArrayList<>(mListeners);
        for (AutonomousFlightControllerListener listener : listenersCpy) {
            listener.onDroneConnectionChanged(state);
        }*/
        mListeners.get(0).onDroneConnectionChanged(state);
    }

    private void notifyBatteryChanged(int battery) {
        mListeners.get(0).onBatteryChargeChanged(battery);
    }

    private void notifyPilotingStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state) {
        mListeners.get(0).onPilotingStateChanged(state);
    }

    private void notifyAutoStateChange(ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM state) {
        mListeners.get(0).onAutoStateChanged(state);
    }

    private void notifyPictureTaken(ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error) {
        mListeners.get(0).onPictureTaken(error);
    }

    private void notifyConfigureDecoder(ARControllerCodec codec) {
        mListeners.get(0).configureDecoder(codec);
    }

    private void notifyFrameReceived(ARFrame frame) {
        mListeners.get(0).onFrameReceived(frame);
    }

    private void notifyMatchingMediasFound(int nbMedias) {
        mListeners.get(0).onMatchingMediasFound(nbMedias);
    }

    private void notifyDownloadProgressed(String mediaName, int progress) {
        mListeners.get(0).onDownloadProgressed(mediaName, progress);
    }

    private void notifyDownloadComplete(String mediaName) {
        mListeners.get(0).onDownloadComplete(mediaName);
    }

    private void notifyGPSChange(double latitude, double longtitude, double altitude) {
        mListeners.get(0).onGPSChange(latitude, longtitude, altitude);
    }

    private void notifySpeedChange(float speedX, float speedY, float speedZ){
        mListeners.get(0).onSpeedChange(speedX, speedY, speedZ);
    }

    private void notifyAltitudeChange(double altitude){
        mListeners.get(0).onAltitudeChange(altitude);
    }

    private void notifyHomeReturn(double latitude, double longtitude, double altitude){
        mListeners.get(0).onHomeReturn(latitude, longtitude, altitude);
    }

    private void notifyAutoFlightPath(ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM component, byte State){
        mListeners.get(0).onFlightPath(component, State);
    }

    private void notifyAvailabiltyState(byte availabilityState) {
        mListeners.get(0).onAutoAvailableState(availabilityState);
    }

    private void onUpdateBebopGpsSatellite(Integer gpsSatellite) {
        mListeners.get(0).numberOfSatellites(gpsSatellite);
    }

    private final ARDeviceControllerStreamListener mStreamListener = new ARDeviceControllerStreamListener() {
        @Override
        public ARCONTROLLER_ERROR_ENUM configureDecoder(ARDeviceController deviceController, final ARControllerCodec codec) {
            notifyConfigureDecoder(codec);
            return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
        }

        @Override
        public ARCONTROLLER_ERROR_ENUM onFrameReceived(ARDeviceController deviceController, final ARFrame frame) {
            notifyFrameReceived(frame);
            return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
        }

        @Override
        public void onFrameTimeout(ARDeviceController deviceController) {}
    };
}