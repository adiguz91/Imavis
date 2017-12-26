package com.drone.imavis.mvp.util.dronecontroll;

import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.dronecontrol.SDCardModule;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.google.android.gms.maps.model.LatLng;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_EVENT_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_VIDEOEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_VIDEOEVENTCHANGED_EVENT_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEOV2_RECORD_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIASTREAMING_VIDEOSTREAMMODE_MODE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED_FRAMERATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_AUTOWHITEBALANCESELECTION_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_PICTUREFORMATSELECTION_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEOFRAMERATE_FRAMERATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEORESOLUTIONS_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_ORIENTATION_MODE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_STATUS_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORNAME_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKPLAYERRORSTATECHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_WIFI_SECURITY_KEY_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DICTIONARY_KEY_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_ERROR_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerArgumentDictionary;
import com.parrot.arsdk.arcontroller.ARControllerCodec;
import com.parrot.arsdk.arcontroller.ARControllerDictionary;
import com.parrot.arsdk.arcontroller.ARControllerException;
import com.parrot.arsdk.arcontroller.ARDeviceController;
import com.parrot.arsdk.arcontroller.ARDeviceControllerListener;
import com.parrot.arsdk.arcontroller.ARDeviceControllerStreamListener;
import com.parrot.arsdk.arcontroller.ARFeatureARDrone3;
import com.parrot.arsdk.arcontroller.ARFeatureCommon;
import com.parrot.arsdk.arcontroller.ARFeatureWifi;
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
import java.util.Iterator;
import java.util.List;

import static com.parrot.arsdk.arcontroller.ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY;

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

    private ARDeviceController mDeviceController;
    private SDCardModule mSDCardModule;
    private ARCONTROLLER_DEVICE_STATE_ENUM mState;
    private ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM mFlyingState;
    private String mCurrentRunId;
    private ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM mAutoFlyingState;
    private ARDiscoveryDeviceService mDeviceService;

    private int mBatteryStatus;
    private String mRunId;
    private boolean mCalibrationIsRequired;

    public AutonomController(Context context, @NonNull ARDiscoveryDeviceService deviceService) {

        mDeviceService = deviceService;
        mListeners = new ArrayList<>();
        // needed because some callbacks will be called on the main thread
        mHandler = new Handler(context.getMainLooper());
        mState = ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED;

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

            ARUtilsManager ftpUploadManager = new ARUtilsManager();
            //ARUtilsManager ftpQueueManager = new ARUtilsManager();

            ftpUploadManager.initWifiFtp(productIP, FTP_FLIGHTPLAN, "", "");
            //ftpUploadManager.initWifiFtp(productIP, FTP_FLIGHTPLAN, ARUtilsManager.FTP_ANONYMOUS, "");
            //ftpQueueManager.initWifiFtp(productIP, FTP_FLIGHTPLAN, ARUtilsManager.FTP_ANONYMOUS, "");

            ARDataTransferManager dataTransferManager = new ARDataTransferManager();
            ARDataTransferUploader uploader = dataTransferManager.getARDataTransferUploader();
            ftpUploadManager = new ARUtilsManager();

            final UploadListener listener = new UploadListener(null, uploader);
            uploader.createUploader(ftpUploadManager, "flightPlan.mavlink", localFilepath, listener, null, listener, null, ARDATATRANSFER_UPLOADER_RESUME_ENUM.ARDATATRANSFER_UPLOADER_RESUME_FALSE);

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

            deviceController.addListener(mDeviceControllerListener);
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

    private final SDCardModule.Listener mSDCardModuleListener = new SDCardModule.Listener() {
        @Override
        public void onMatchingMediasFound(final int nbMedias) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyMatchingMediasFound(nbMedias);
                }
            });
        }

        @Override
        public void onDownloadProgressed(final String mediaName, final int progress) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyDownloadProgressed(mediaName, progress);
                }
            });
        }

        @Override
        public void onDownloadComplete(final String mediaName) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyDownloadComplete(mediaName);
                }
            });
        }
    };

    private final ARDeviceControllerListener mDeviceControllerListener = new ARDeviceControllerListener() {
        @Override
        public void onStateChanged(ARDeviceController deviceController, ARCONTROLLER_DEVICE_STATE_ENUM newState, ARCONTROLLER_ERROR_ENUM error) {
            mState = newState;
            if (ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING.equals(mState)) {
                //mDeviceController.getFeatureARDrone3().sendMediaStreamingVideoEnable((byte) 1);
            } else if (ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED.equals(mState)) {
                mSDCardModule.cancelGetFlightMedias();
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyConnectionChanged(mState);
                }
            });
        }

        @Override
        public void onExtensionStateChanged(ARDeviceController deviceController, ARCONTROLLER_DEVICE_STATE_ENUM newState, ARDISCOVERY_PRODUCT_ENUM product, String name, ARCONTROLLER_ERROR_ENUM error) {
        }

        @Override
        public void onCommandReceived(ARDeviceController deviceController, ARCONTROLLER_DICTIONARY_KEY_ENUM commandKey, ARControllerDictionary elementDictionary) {
            Log.d("onCommandReceived", commandKey.toString());
            if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_BATTERYSTATECHANGED) && (elementDictionary != null)) {
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final int battery = (Integer) args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_BATTERYSTATECHANGED_PERCENT);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyBatteryChanged(battery);
                        }
                    });
                }
            }
            if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXDISTANCECHANGED) && (elementDictionary != null)){
                // SetMaxDistance
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    float current = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXDISTANCECHANGED_CURRENT)).doubleValue();
                    float min = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXDISTANCECHANGED_MIN)).doubleValue();
                    float max = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXDISTANCECHANGED_MAX)).doubleValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // http://developer.parrot.com/docs/reference/bebop_2/#max-distance
                            notifyChangeSetMaxDistance(current, min, max);
                        }
                    });
                }
            }
            if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED) && (elementDictionary != null)){
                // ChangeMoveTo
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    double latitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_LATITUDE);
                    double longitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_LONGITUDE);
                    double altitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_ALTITUDE);
                    ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_ORIENTATION_MODE_ENUM orientationMode = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_ORIENTATION_MODE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_ORIENTATION_MODE));
                    float heading = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_HEADING)).doubleValue();
                    ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_STATUS_ENUM status = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_STATUS_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_STATUS));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // http://developer.parrot.com/docs/reference/bebop_2/#move-to-changed
                            notifyChangeMoveTo(new GPSCoordinate(latitude, longitude, altitude),
                                               orientationMode, heading, status);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_CURRENTDATECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#date-changed
                //setCurrentDateTime();
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    String date = (String)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_CURRENTDATECHANGED_DATE);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeCurrentDate(); // waiting for time
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_CURRENTTIMECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#time-changed
                //setCurrentDateTime();
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    String time = (String)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_CURRENTTIMECHANGED_TIME);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeCurrentDate(); // waiting for time
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED) && (elementDictionary != null)){
                //setPictureAndVideoSettings();
                // http://developer.parrot.com/docs/reference/bebop_2/#video-resolutions
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED_TYPE_ENUM type = ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED_TYPE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED_TYPE));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangePictureAndVideoSettings(type); // waiting
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED) && (elementDictionary != null)){
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED_FRAMERATE_ENUM framerate = ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED_FRAMERATE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED_FRAMERATE));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangePictureAndVideoSettings(framerate); // waiting
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_VIDEOAUTORECORDCHANGED) && (elementDictionary != null)){
                //enableAutoVideoRecord();
                // http://developer.parrot.com/docs/reference/bebop_2/#video-autorecord-mode
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte enabled = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_VIDEOAUTORECORDCHANGED_ENABLED)).intValue();
                    byte mass_storage_id = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_VIDEOAUTORECORDCHANGED_MASS_STORAGE_ID)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeEnableAutoVideoRecord();
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_RETURNHOMEDELAYCHANGED) && (elementDictionary != null)){
                //setReturnHomeDelayAfterDisconnect();
                // http://developer.parrot.com/docs/reference/bebop_2/#return-home-delay
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    short delay = (short)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_RETURNHOMEDELAYCHANGED_DELAY)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeReturnHomeDelayAfterDisconnect();
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED) && (elementDictionary != null)){
                //setHomeType();
                // http://developer.parrot.com/docs/reference/bebop_2/#preferred-home-type
                // AND optional http://developer.parrot.com/docs/reference/bebop_2/#home-type-availability
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM type = ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeHomeType();
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANSETTINGSSTATE_RETURNHOMEONDISCONNECTCHANGED) && (elementDictionary != null)){
                //setReturnHomeOnDisconnect();
                // http://developer.parrot.com/docs/reference/bebop_2/#returnhome-behavior-during-flightplan
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte state = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANSETTINGSSTATE_RETURNHOMEONDISCONNECTCHANGED_STATE)).intValue();
                    byte isReadOnly = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANSETTINGSSTATE_RETURNHOMEONDISCONNECTCHANGED_ISREADONLY)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeReturnHomeOnDisconnect(state, isReadOnly);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_SETTINGSSTATE_COUNTRYCHANGED) && (elementDictionary != null)){
                //setWifiSettingsCountry();
                // http://developer.parrot.com/docs/reference/bebop_2/#set-the-country
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    String codeCountry = (String)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_SETTINGSSTATE_COUNTRYCHANGED_CODE);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeSetWifiSettingsCountry(codeCountry);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_SETTINGSSTATE_AUTOCOUNTRYCHANGED) && (elementDictionary != null)){
                // setWifiSettingsAutoCountry();
                // http://developer.parrot.com/docs/reference/bebop_2/#auto-country-changed
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte automatic = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_SETTINGSSTATE_AUTOCOUNTRYCHANGED_AUTOMATIC)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeSetWifiSettingsAutoCountry(automatic);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_WIFI_SECURITYCHANGED) && (elementDictionary != null)){
                //setWifiSecurity();
                // http://developer.parrot.com/docs/reference/bebop_2/#wifi-security-changed
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    String key = (String)args.get(ARFeatureWifi.ARCONTROLLER_DICTIONARY_KEY_WIFI_SECURITYCHANGED_KEY);
                    ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM key_type = ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM.getFromValue((Integer)args.get(ARFeatureWifi.ARCONTROLLER_DICTIONARY_KEY_WIFI_SECURITYCHANGED_KEY_TYPE));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeSetWifiSecurity(key);
                        }
                    });
                }
            }
            /*else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED) && (elementDictionary != null)){
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM state = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED_STATE));
                }
            }*/
            else if (commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED){
                // SENSORS STATE LIST
                // http://developer.parrot.com/docs/reference/bebop_2/#sensors-state-list
                if ((elementDictionary != null) && (elementDictionary.size() > 0)) {
                    Iterator<ARControllerArgumentDictionary<Object>> itr = elementDictionary.values().iterator();
                    while (itr.hasNext()) {
                        ARControllerArgumentDictionary<Object> args = itr.next();
                        if (args != null) {
                            ARCOMMANDS_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORNAME_ENUM sensorName = ARCOMMANDS_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORNAME_ENUM.getFromValue((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORNAME));
                            byte sensorState = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORSTATE)).intValue();
                        }
                    }
                } else {
                    // list is empty
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#video-stream-state
                // enableVideoStreaming();
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM enabledStatus = ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeEnableVideoStreaming(enabledStatus);
                        }
                    });
                }
            }
            else if (commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_MASSSTORAGECONTENTFORCURRENTRUN){
                // MASS STORAGE CONTENT FOR CURRENT RUN; runId; count pictueres
                // http://developer.parrot.com/docs/reference/bebop_2/#mass-storage-content
                if ((elementDictionary != null) && (elementDictionary.size() > 0)) {
                    Iterator<ARControllerArgumentDictionary<Object>> itr = elementDictionary.values().iterator();
                    while (itr.hasNext()) {
                        ARControllerArgumentDictionary<Object> args = itr.next();
                        if (args != null) {
                            byte mass_storage_id = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_MASSSTORAGECONTENTFORCURRENTRUN_MASS_STORAGE_ID)).intValue();
                            short nbPhotos = (short)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_MASSSTORAGECONTENTFORCURRENTRUN_NBPHOTOS)).intValue();
                            short nbVideos = (short)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_MASSSTORAGECONTENTFORCURRENTRUN_NBVIDEOS)).intValue();
                            short nbRawPhotos = (short)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_MASSSTORAGECONTENTFORCURRENTRUN_NBRAWPHOTOS)).intValue();
                        }
                    }
                } else {
                    // list is empty
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED) && (elementDictionary != null)){
                // RETURN HOME STATE
                // ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED
                // returnHome();
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE_ENUM state = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE));
                    ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON_ENUM reason = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeReturnHomeState(state, reason);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED) && (elementDictionary != null)) {
                // http://developer.parrot.com/docs/reference/bebop_2/#flying-state
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.getFromValue((Integer) args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mFlyingState = state;
                            notifyFlyingStateChanged(state);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#picture-state
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM state = ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE));
                    ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM error = ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR));
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#video-record-state
                // AND (optional http://developer.parrot.com/docs/reference/bebop_2/#video-record-notification)
                // recordVideoOrTakePictures();
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM state = ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE));
                    ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM error = ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeRecordVideoOrTakePictures(state, error);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#picture-taken
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error = ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyPictureTaken(error);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_RUNSTATE_RUNIDCHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#current-run-id
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final String runId = (String) args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_RUNSTATE_RUNIDCHANGED_RUNID);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mRunId = runId;
                            // notifyChangeCurrentRunId(runId);
                        }
                    });
                }
            }
            /*else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED) && (elementDictionary != null)){
                // deprecated!!!
                // http://developer.parrot.com/docs/reference/bebop_2/#drone-39-s-position-changed
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if(args != null){
                    final double latitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED_LATITUDE);
                    final double longtitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED_LONGITUDE);
                    final double altitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED_ALTITUDE);
                    //Log.d(TAG, "Position changed lat= " + String.valueOf(latitude) + ", lon= " + String.valueOf(longtitude) + ", alt= " + String.valueOf(altitude));
                    mHandler.post(new Runnable(){
                        @Override
                        public void run() {
                            notifyGPSChange(latitude,longtitude,altitude);
                        }
                    });
                }
            }*/
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_SPEEDCHANGED) && (elementDictionary != null)) {
                // http://developer.parrot.com/docs/reference/bebop_2/#drone-39-s-speed-changed
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final float speedX = (float) ((Double) args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_SPEEDCHANGED_SPEEDX)).doubleValue();
                    final float speedY = (float) ((Double) args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_SPEEDCHANGED_SPEEDY)).doubleValue();
                    final float speedZ = (float) ((Double) args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_SPEEDCHANGED_SPEEDZ)).doubleValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifySpeedChange(speedX, speedY, speedZ);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_GPSLOCATIONCHANGED) && (elementDictionary != null)){
                // DRONE’S LOCATION CHANGED, replaces PositionChange
                // http://developer.parrot.com/docs/reference/bebop_2/#drone-39-s-location-changed
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    double latitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_GPSLOCATIONCHANGED_LATITUDE);
                    double longitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_GPSLOCATIONCHANGED_LONGITUDE);
                    double altitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_GPSLOCATIONCHANGED_ALTITUDE);
                    byte latitude_accuracy = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_GPSLOCATIONCHANGED_LATITUDE_ACCURACY)).intValue();
                    byte longitude_accuracy = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_GPSLOCATIONCHANGED_LONGITUDE_ACCURACY)).intValue();
                    byte altitude_accuracy = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_GPSLOCATIONCHANGED_ALTITUDE_ACCURACY)).intValue();
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXALTITUDECHANGED) && (elementDictionary != null)){
                //setMaxAltitude();
                // http://developer.parrot.com/docs/reference/bebop_2/#max-altitude
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    float current = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXALTITUDECHANGED_CURRENT)).doubleValue();
                    float min = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXALTITUDECHANGED_MIN)).doubleValue();
                    float max = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXALTITUDECHANGED_MAX)).doubleValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeMaxAltitude(current, min, max);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#motor-error
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte motorIds = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORIDS)).intValue();
                    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM motorError = ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeMotorError(motorIds, motorError);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED) && (elementDictionary != null)){
                // setPictureFormat
                // http://developer.parrot.com/docs/reference/bebop_2/#picture-format
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE_ENUM type = ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE));
                    mHandler.post(new Runnable() {
                        @Override public void run() {
                            notifyChangeSetPictureFormat(type);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED) && (elementDictionary != null)){
                // setWitheBalanceMode();
                // http://developer.parrot.com/docs/reference/bebop_2/#white-balance-mode
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE_ENUM type = ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE));
                    mHandler.post(new Runnable() {
                        @Override public void run() {
                            notifyChangeSetWitheBalanceMode(type);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_TIMELAPSECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#timelapse-mode
                // setPictureInterval();
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte enabled = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_TIMELAPSECHANGED_ENABLED)).intValue();
                    float interval = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_TIMELAPSECHANGED_INTERVAL)).doubleValue();
                    float minInterval = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_TIMELAPSECHANGED_MININTERVAL)).doubleValue();
                    float maxInterval = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_TIMELAPSECHANGED_MAXINTERVAL)).doubleValue();
                    mHandler.post(new Runnable() {
                        @Override public void run() {
                            notifyChangeSetPictureInterval(enabled, interval, minInterval, maxInterval);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_GPSFIXSTATECHANGED) && (elementDictionary != null)){
                //setHomeLocation
                // http://developer.parrot.com/docs/reference/bebop_2/#gps-fix-info
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte fixed = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_GPSFIXSTATECHANGED_FIXED)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeHomeLocation(fixed);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORFLIGHTSSTATUSCHANGED) && (elementDictionary != null)){
                // MOTOR FLIGHT STATUS !!! Important!!!
                // http://developer.parrot.com/docs/reference/bebop_2/#motor-flight-status
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    short nbFlights = (short)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORFLIGHTSSTATUSCHANGED_NBFLIGHTS)).intValue();
                    short lastFlightDuration = (short)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORFLIGHTSSTATUSCHANGED_LASTFLIGHTDURATION)).intValue();
                    int totalFlightDuration = (int)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORFLIGHTSSTATUSCHANGED_TOTALFLIGHTDURATION);
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#last-motor-error
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM motorError = ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR));
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_NOFLYOVERMAXDISTANCECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#geofencing
                // IMPORTANT !!!
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte shouldNotFlyOver = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_NOFLYOVERMAXDISTANCECHANGED_SHOULDNOTFLYOVER)).intValue();
                }
            }
            /*
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_RETURNHOMEBATTERYCAPACITY) && (elementDictionary != null)){
                // RETURN HOME BATTERY CAPACITY
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_PILOTINGSTATE_RETURNHOMEBATTERYCAPACITY_STATUS_ENUM status = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_RETURNHOMEBATTERYCAPACITY_STATUS_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_RETURNHOMEBATTERYCAPACITY_STATUS));
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_MOTIONSTATE) && (elementDictionary != null)){
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOTIONSTATE_STATE_ENUM state = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOTIONSTATE_STATE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_MOTIONSTATE_STATE));
                }
            }*/
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_ALTITUDECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#drone-39-s-attitude-changed
                // from takeOff point to altitude - height
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final double altitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_ALTITUDECHANGED_ALTITUDE);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyAltitudeChange(altitude);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_HOMECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#home-location
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final double latitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_HOMECHANGED_LATITUDE);
                    final double longitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_HOMECHANGED_LONGITUDE);
                    final double altitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_HOMECHANGED_ALTITUDE);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyHomeReturn(latitude, longitude, altitude);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#flightplan-components-state-list
                Iterator<ARControllerArgumentDictionary<Object>> itr = elementDictionary.values().iterator();
                while (itr.hasNext()) {
                    ARControllerArgumentDictionary<Object> args = itr.next();
                    if (args != null) {
                        final ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM component = ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM.getFromValue((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT));

                        final byte State = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_STATE)).intValue();
                        Log.d("componentDrone", "STATE: " + State + ", VALUE: " + component.getValue() + ": " + component.toString());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                notifyAutoFlightPath(component, State);
                            }
                        });
                    }
                }
            }
            /*else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANEVENT_STARTINGERROREVENT) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#flightplan-start-error
                // FLIGHTPLAN START ERROR
            }*/
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANSTATE_AVAILABILITYSTATECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#flightplan-availability
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final byte AvailabilityState = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANSTATE_AVAILABILITYSTATECHANGED_AVAILABILITYSTATE)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyAvailabiltyState(AvailabilityState);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED) && (elementDictionary != null)){
                //startAutonomousFlight();
                //stopAutonomousFlight();
                //pauseAutonomousFlight();
                // http://developer.parrot.com/docs/reference/bebop_2/#playing-state-of-a-flightplan
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM state = ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM.getFromValue((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE));
                    String filepath = (String)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_FILEPATH);
                    ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE_ENUM type = ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE_ENUM.getFromValue((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAutoFlyingState = state;
                            //notifyAutoStateChange(state);
                            notifyChangeAutonomousFlight(state, type, filepath);
                        }
                    });
                }
            }
            /*else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_MAVLINKSTATE_MAVLINKPLAYERRORSTATECHANGED) && (elementDictionary != null)){
                // flyplan error message
                // http://developer.parrot.com/docs/reference/bebop_2/#flightplan-error-deprecated
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKPLAYERRORSTATECHANGED_ERROR_ENUM error = ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKPLAYERRORSTATECHANGED_ERROR_ENUM.getFromValue((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_MAVLINKSTATE_MAVLINKPLAYERRORSTATECHANGED_ERROR));
                }
            }*/
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONREQUIREDSTATE) && (elementDictionary != null)){
                // CALIBRATION REQUIRED
                // http://developer.parrot.com/docs/reference/bebop_2/#calibration-required
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte required = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONREQUIREDSTATE_REQUIRED)).intValue();
                    mCalibrationIsRequired = required == 1 ? true:false;
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED) && (elementDictionary != null)){
                // AXIS TO CALIBRATE DURING CALIBRATION PROCESS
                // http://developer.parrot.com/docs/reference/bebop_2/#axis-to-calibrate-during-calibration-process
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS_ENUM axis = ARCOMMANDS_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS_ENUM.getFromValue((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeCalibrateAxis(axis);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTARTEDCHANGED) && (elementDictionary != null)){
                //calibration();
                // http://developer.parrot.com/docs/reference/bebop_2/#calibration-process-state
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte started = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTARTEDCHANGED_STARTED)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyChangeCalibrationState();
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATECHANGED) && (elementDictionary != null)){
                // MAGNETO CALIB PROCESS AXIS STATE
                // http://developer.parrot.com/docs/reference/bebop_2/#magneto-calib-process-axis-state
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte xAxisCalibration = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATECHANGED_XAXISCALIBRATION)).intValue();
                    byte yAxisCalibration = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATECHANGED_YAXISCALIBRATION)).intValue();
                    byte zAxisCalibration = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATECHANGED_ZAXISCALIBRATION)).intValue();
                    byte calibrationFailed = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATECHANGED_CALIBRATIONFAILED)).intValue();
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_MAVLINKSTATE_MISSIONITEMEXECUTED) && (elementDictionary != null)){
                // MISSION ITEM EXECUTED
                // http://developer.parrot.com/docs/reference/bebop_2/#mission-item-executed
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    int idx = (int)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_MAVLINKSTATE_MISSIONITEMEXECUTED_IDX);
                }
            }
            else if (commandKey ==  ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSTATE_NUMBEROFSATELLITECHANGED)
            {
                // http://developer.parrot.com/docs/reference/bebop_2/#number-of-gps-satellites
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final Integer gpsSatellite = (Integer)  args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSTATE_NUMBEROFSATELLITECHANGED_NUMBEROFSATELLITE);
                    Log.d("componentDrone", "gpsSatellite: " + Integer.toString(gpsSatellite));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onUpdateBebopGpsSatellite(gpsSatellite);  // show value in TextView
                        }
                    });
                }
            }
        }
    };

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