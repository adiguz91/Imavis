package com.drone.imavis.mvp.util.dronecontroll;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.dronecontrol.SDCardModule;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Nodes;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.WaypointData;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.WaypointMode;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
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
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_ORIENTATION_MODE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_STATUS_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE_ENUM;
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
import com.parrot.arsdk.armavlink.MAV_ROI;
import com.parrot.arsdk.arutils.ARUtilsException;
import com.parrot.arsdk.arutils.ARUtilsManager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by adigu on 14.09.2017.
 */

// http://developer.parrot.com/docs/reference/bebop_2/index.html
public class AutonomousFlightController implements IAutonomousFlightController, DroneDeviceControllerListener {

    // constants
    public static final String DATE_FORMAT_ISO_8601 = "yyyy-mm-dd";
    public static final String TIME_FORMAT_ISO_8601 = "hh:mm:ss";
    public static final String MAVLINK_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/mavlink_files";
    private static final String TAG = "BebopDrone";
    private static final int FTP_FLIGHTPLAN = 61; //21

    private SimpleDateFormat formatterISO8601;

    private String mCurrentDate;
    private String mCurrentTime;
    private boolean mIsChangedCurrentDate;
    private boolean mIsChangedCurrentTime;

    private boolean mIsChangedPictureAndVideoFramerate;
    private boolean mIsChangedPictureAndVideoResolution;
    private ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED_TYPE_ENUM mPictureAndVideoResolutionType;
    private ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED_FRAMERATE_ENUM mPictureAndVideoFramerateType;

    private AutonomousFlightControllerListener mListener;
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
    private DroneDeviceControllerManager droneDeviceController;

    public AutonomousFlightController(Context context, @NonNull ARDiscoveryDeviceService deviceService) throws ARUtilsException {

        formatterISO8601 = new SimpleDateFormat(DATE_FORMAT_ISO_8601 + " " + TIME_FORMAT_ISO_8601);

        mFtpUploadManager = new ARUtilsManager();
        mFtpQueueManager = new ARUtilsManager();
        mDeviceService = deviceService;
        // needed because some callbacks will be called on the main thread
        mHandler = new Handler(context.getMainLooper());
        mState = ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED;

        mSDCardModule = new SDCardModule(mFtpUploadManager, mFtpQueueManager);
        droneDeviceController = new DroneDeviceControllerManager(context, mSDCardModule);

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

    private Date getDateTimeFromString(String source) {
        Date dateTime = null;
        try {
            dateTime = formatterISO8601.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
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

    public void dispose()
    {
        if (mDeviceController != null)
            mDeviceController.dispose();
    }

    public void setListener(AutonomousFlightControllerListener listener) {
        mListener = listener;
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
        mIsChangedCurrentDate = false;
        mIsChangedCurrentTime = false;

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
        mIsChangedPictureAndVideoFramerate = false;
        mIsChangedPictureAndVideoResolution = false;

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

    public String generateMavlinkFile(Nodes nodes, short delayBeforStart) {

        if (nodes.getWaypoints() == null || nodes.getWaypoints().size() == 0) {
            return null;
        }

        final ARMavlinkFileGenerator generator;

        try {
            generator = new ARMavlinkFileGenerator();
        } catch (ARMavlinkException e) {
            return null;
        }

        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkDelay(delayBeforStart));

        if (nodes.getWaypoints().size() <= 2) {
            GPSCoordinate takeOff = new GPSCoordinate(); // TODO nodes.getWaypoints().getFirst();
            generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkTakeoffMissionItem((float)takeOff.getLatitude(), (float)takeOff.getLongitude(), (float)takeOff.getAltitude(), 0, 0));
            nodes.getWaypoints().getFirst();
            GPSCoordinate landing = new GPSCoordinate(); // TODO nodes.getWaypoints().getLast();
            generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkLandMissionItem((float)landing.getLatitude(), (float)landing.getLongitude(), (float)landing.getAltitude(), 0));
        }

        int count = 0;
        for (Waypoint waypoint : nodes.getWaypoints()) {
            if ((0 < count) && (count < (nodes.getWaypoints().size() - 1))) {
                // convert coordinate to gpsCoordinates waypoint.getShape().getCoordinate(); lat == y; lng == x; alt == z
                GPSCoordinate gpsCoordinate = new GPSCoordinate();
                generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkNavWaypointMissionItem((float)gpsCoordinate.getLatitude(), (float)gpsCoordinate.getLongitude(), (float)gpsCoordinate.getAltitude(), 0));

                if (((WaypointData)waypoint.getData()).getPoi() != null) {
                    //generator.GetCurrentMissionItemList().getMissionItem(count).
                    GPSCoordinate poiCoordinate = new GPSCoordinate();
                    // TODO ROI and SetViewMode
                    if (((WaypointData) waypoint.getData()).getMode() == WaypointMode.Progressive) {
                        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkSetROI(MAV_ROI.MAV_ROI_WPNEXT, 0, 0, 0, 0, 0));
                    }
                    else {
                        generator.addMissionItem(ARMavlinkMissionItem.CreateMavlinkSetROI(MAV_ROI.MAV_ROI_LOCATION, 0, 0, (float)poiCoordinate.getLatitude(), (float)poiCoordinate.getLongitude(), (float)poiCoordinate.getAltitude()));
                    }
                }
            }
            count++;
        }

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

    // ################################

    private final ARDeviceControllerStreamListener mStreamListener = new ARDeviceControllerStreamListener() {
        @Override
        public ARCONTROLLER_ERROR_ENUM configureDecoder(ARDeviceController deviceController, final ARControllerCodec codec) {
            //notifyConfigureDecoder(codec);
            return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
        }

        @Override
        public ARCONTROLLER_ERROR_ENUM onFrameReceived(ARDeviceController deviceController, final ARFrame frame) {
            //notifyFrameReceived(frame);
            return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
        }

        @Override
        public void onFrameTimeout(ARDeviceController deviceController) {}
    };

    // ####################################################
    // Listener begin

    public void notifyMatchingMediasFoundChanged(int numberOfMedias) {
        mListener.notifyMatchingMediasFoundChanged(numberOfMedias);
    }

    public void notifyDownloadProgressedChanged(String mediaName, int progress) {
        mListener.notifyDownloadProgressedChanged(mediaName, progress);
    }

    public void notifyDownloadCompleteChanged(String mediaName) {
        mListener.notifyDownloadCompleteChanged(mediaName);
    }

    public void notifyConnectionStateChanged(ARCONTROLLER_DEVICE_STATE_ENUM state) {
        mListener.notifyConnectionStateChanged(state);
    }

    public void notifyBatteryProgressChanged(int batteryProgress) {
        mListener.notifyBatteryProgressChanged(batteryProgress);
    }

    public void notifyMaxDistanceChanged(float current, float min, float max) {
        mListener.notifyMaxDistanceChanged(current, min, max);
    }

    public void notifyMoveToChanged(GPSCoordinate location, float heading, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_ORIENTATION_MODE_ENUM orientationMode, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_STATUS_ENUM status) {
        mListener.notifyMoveToChanged(location, heading, orientationMode, status);
    }

    public void notifyCurrentDateChanged(String date) {
        mCurrentDate = date;
        mIsChangedCurrentDate = true;
        if (mIsChangedCurrentTime) {
            mListener.notifyCurrentDateTimeChanged(getDateTimeFromString(mCurrentDate + " " + mCurrentTime));
        }
    }

    public void notifyCurrentTimeChanged(String time) {
        mCurrentTime = time;
        mIsChangedCurrentTime = true;
        if (mIsChangedCurrentDate) {
            mListener.notifyCurrentDateTimeChanged(getDateTimeFromString(mCurrentDate + " " + mCurrentTime));
        }
    }

    public void notifyPictureAndVideoResolutionChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED_TYPE_ENUM type) {
        mPictureAndVideoResolutionType = type;
        mIsChangedPictureAndVideoResolution = true;
        if (mIsChangedPictureAndVideoFramerate) {
            mListener.notifyPictureAndVideoSettingsChanged(mPictureAndVideoResolutionType, mPictureAndVideoFramerateType);
        }
    }

    public void notifyPictureAndVideoFramerateChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED_FRAMERATE_ENUM framerate) {
        mPictureAndVideoFramerateType = framerate;
        mIsChangedPictureAndVideoFramerate = true;
        if (mIsChangedPictureAndVideoResolution) {
            mListener.notifyPictureAndVideoSettingsChanged(mPictureAndVideoResolutionType, mPictureAndVideoFramerateType);
        }
    }

    public void notifyAutoVideoRecordStatusChanged(boolean enabled, byte massStorageId) {
        mListener.notifyAutoVideoRecordStatusChanged(enabled, massStorageId);
    }

    public void notifyReturnHomeDelayAfterDisconnectChanged(short delay) {
        mListener.notifyReturnHomeDelayAfterDisconnectChanged(delay);
    }

    public void notifyHomeTypeChanged(ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM type) {
        mListener.notifyHomeTypeChanged(type);
    }

    public void notifyReturnHomeOnDisconnectStatusChanged(boolean enabled, boolean isReadOnly) {
        mListener.notifyReturnHomeOnDisconnectStatusChanged(enabled, isReadOnly);
    }

    public void notifyWifiSettingsCountryChanged(String countryCode) {
        mListener.notifyWifiSettingsCountryChanged(countryCode);
    }

    public void notifyWifiSettingsAutoCountryStatusChanged(boolean automatic) {
        mListener.notifyWifiSettingsAutoCountryStatusChanged(automatic);
    }

    public void notifyWifiSecurityChanged(String key, ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM keyType) {
        mListener.notifyWifiSecurityChanged(key, keyType);
    }

    public void notifyVideoStreamingStatusChanged(ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM enabledStatus) {
        mListener.notifyVideoStreamingStatusChanged(enabledStatus);
    }

    public void notifyMassStorageContentChanged(Map<Byte, Short> massContentTypeCount) {
        mListener.notifyMassStorageContentChanged(massContentTypeCount);
    }

    public void notifyReturnHomeStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE_ENUM state, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON_ENUM reason) {
        mListener.notifyReturnHomeStateChanged(state, reason);
    }

    public void notifyFlyingStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state) {
        mListener.notifyFlyingStateChanged(state);
        // TODO LOCAL VARIABLE?
    }

    public void notifyPictureStateChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM state, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM error) {
        mListener.notifyPictureStateChanged(state, error);
    }

    public void notifyRecordVideoOrTakePicturesStateChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM state, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM error) {
        mListener.notifyRecordVideoOrTakePicturesStateChanged(state, error);
    }

    public void notifyPictureTakenChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error) {
        mListener.notifyPictureTakenChanged(error);
    }

    public void notifyCurrentRunIdChanged(String runId) {
        mListener.notifyCurrentRunIdChanged(runId);
    }

    public void notifySpeedChanged(float speedX, float speedY, float speedZ) {
        mListener.notifySpeedChanged(speedX, speedY, speedZ);
    }

    public void notifyDroneLocationChanged(GPSCoordinate location, byte latitudeAccuracy, byte longitudeAccuracy, byte altitudeAccuracy) {
        mListener.notifyDroneLocationChanged(location, latitudeAccuracy, longitudeAccuracy, altitudeAccuracy);
    }

    public void notifyMaxAltitudeChanged(float current, float min, float max) {
        mListener.notifyMaxAltitudeChanged(current, min, max);
    }

    public void notifyMotorErrorChanged(byte motorIds, ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM motorError) {
        mListener.notifyMotorErrorChanged(motorIds, motorError);
    }

    public void notifyPictureFormatChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE_ENUM type) {
        mListener.notifyPictureFormatChanged(type);
    }

    public void notifyWitheBalanceModeChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE_ENUM type) {
        //mListener.notifyWitheBalanceModeChanged(type);
        throw new UnsupportedOperationException();
    }

    public void notifyPictureIntervalChanged(boolean enabled, float interval, float minInterval, float maxInterval) {
        mListener.notifyPictureIntervalChanged(enabled, interval, minInterval, maxInterval);
    }

    public void notifyHomeLocationChanged(boolean isFixed) {
        mListener.notifyHomeLocationChanged(isFixed);
    }

    public void notifyMotorFlightStatusChanged(short numberOfFlights, short lastFlightDuration, int totalFlightDuration) {
        mListener.notifyMotorFlightStatusChanged(numberOfFlights, lastFlightDuration, totalFlightDuration);
    }

    public void notifyLastMotorErrorChanged(ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM motorError) {
        mListener.notifyLastMotorErrorChanged(motorError);
    }

    public void notifyGeofencingChanged(boolean shouldNotFlyOver) {
        mListener.notifyGeofencingChanged(shouldNotFlyOver);
    }

    public void notifyAltitudeChanged(double altitude) {
        mListener.notifyAltitudeChanged(altitude);
    }

    public void notifyHomeLocationChanged(GPSCoordinate location) {
        mListener.notifyHomeLocationChanged(location);
    }

    public void notifyFlightPlanComponentStateListChanged(ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM component, boolean state) {
        mListener.notifyFlightPlanComponentStateListChanged(component, state);
    }

    public void notifyFlightPlanAvailabilityStateChanged(boolean state) {
        mListener.notifyFlightPlanAvailabilityStateChanged(state);
    }

    public void notifyAutonomousFlightChanged(ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM state, ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE_ENUM type, String filepath) {
        mListener.notifyAutonomousFlightChanged(state, type, filepath);
    }

    public void notifyCalibrationRequiredChanged(boolean isRequired) {
        mListener.notifyCalibrationRequiredChanged(isRequired);
    }

    public void notifyCalibrationAxisChanged(ARCOMMANDS_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS_ENUM axis) {
        mListener.notifyCalibrationAxisChanged(axis);
    }

    public void notifyCalibrationStateChanged(boolean started) {
        mListener.notifyCalibrationStateChanged(started);
    }

    public void notifyCalibrationAxisStateChanged(boolean xAxisIsCalibrated, boolean yAxisIsCalibrated, boolean zAxisIsCalibrated, boolean calibrationFailed) {
        mListener.notifyCalibrationAxisStateChanged(xAxisIsCalibrated, yAxisIsCalibrated, zAxisIsCalibrated, calibrationFailed);
    }

    public void notifyMissionItemExecutedChanged(int missionItemId) {
        mListener.notifyMissionItemExecutedChanged(missionItemId);
    }

    public void notifyNumberOfSatellitesChanged(int statellites) {
        mListener.notifyNumberOfSatellitesChanged(statellites);
    }

    // Listener end
    // ####################################################
}