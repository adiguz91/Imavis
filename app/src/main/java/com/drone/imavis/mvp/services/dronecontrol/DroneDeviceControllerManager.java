package com.drone.imavis.mvp.services.dronecontrol;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.drone.imavis.mvp.services.dronecontrol.bebopexamples.SDCardModule;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.drone.imavis.mvp.util.extensions.BooleanExtension;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED_FRAMERATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED_TYPE_ENUM;
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
import com.parrot.arsdk.arcommands.ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DICTIONARY_KEY_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_ERROR_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerArgumentDictionary;
import com.parrot.arsdk.arcontroller.ARControllerDictionary;
import com.parrot.arsdk.arcontroller.ARDeviceController;
import com.parrot.arsdk.arcontroller.ARDeviceControllerListener;
import com.parrot.arsdk.arcontroller.ARFeatureARDrone3;
import com.parrot.arsdk.arcontroller.ARFeatureCommon;
import com.parrot.arsdk.arcontroller.ARFeatureWifi;
import com.parrot.arsdk.ardiscovery.ARDISCOVERY_PRODUCT_ENUM;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.parrot.arsdk.arcontroller.ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY;

/**
 * Created by adigu on 26.12.2017.
 */

public class DroneDeviceControllerManager {

    private final Handler mHandler;
    private DroneDeviceControllerListener listener;
    private final SDCardModule mSDCardModule;

    public DroneDeviceControllerManager(Context context, SDCardModule sdCardModule) {
        mHandler = new Handler(context.getMainLooper());
        mSDCardModule = sdCardModule;
        mSDCardModule.addListener(mSDCardModuleListener);
    }

    public void setListener(DroneDeviceControllerListener listener) {
        this.listener = listener;
    }

    public ARDeviceControllerListener getDeviceControllerListener() {
        return mDeviceControllerListener;
    }

    /*
    public SDCardModule.Listener getSDCardModuleListener() {
        return mSDCardModuleListener;
    }
    */

    private final SDCardModule.Listener mSDCardModuleListener = new SDCardModule.Listener() {
        @Override
        public void onMatchingMediasFound(final int nbMedias) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.notifyMatchingMediasFoundChanged(nbMedias);
                }
            });
        }

        @Override
        public void onDownloadProgressed(final String mediaName, final int progress) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.notifyDownloadProgressedChanged(mediaName, progress);
                }
            });
        }

        @Override
        public void onDownloadComplete(final String mediaName) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.notifyDownloadCompleteChanged(mediaName);
                }
            });
        }
    };

    private final ARDeviceControllerListener mDeviceControllerListener = new ARDeviceControllerListener() {
        @Override
        public void onStateChanged(ARDeviceController deviceController, ARCONTROLLER_DEVICE_STATE_ENUM newState, ARCONTROLLER_ERROR_ENUM error) {
            if (ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING.equals(newState)) {
                //mDeviceController.getFeatureARDrone3().sendMediaStreamingVideoEnable((byte) 1);
            } else if (ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED.equals(newState)) {
                mSDCardModule.cancelGetFlightMedias();
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.notifyConnectionStateChanged(newState);
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
                // http://developer.parrot.com/docs/reference/bebop_2/#battery-state
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final int battery = (Integer) args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_BATTERYSTATECHANGED_PERCENT);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyBatteryProgressChanged(battery);
                        }
                    });
                }
            }
            if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXDISTANCECHANGED) && (elementDictionary != null)){
                // SetMaxDistance
                // http://developer.parrot.com/docs/reference/bebop_2/#max-distance
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    float current = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXDISTANCECHANGED_CURRENT)).doubleValue();
                    float min = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXDISTANCECHANGED_MIN)).doubleValue();
                    float max = (float)((Double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_MAXDISTANCECHANGED_MAX)).doubleValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyMaxDistanceChanged(current, min, max);
                        }
                    });
                }
            }
            if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED) && (elementDictionary != null)){
                // ChangeMoveTo
                // http://developer.parrot.com/docs/reference/bebop_2/#move-to-changed
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
                            listener.notifyMoveToChanged(new GPSCoordinate(latitude, longitude, altitude), heading,
                                                                        orientationMode, status);
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
                            listener.notifyCurrentDateChanged(date); // waiting for time
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
                            listener.notifyCurrentTimeChanged(time); // waiting for time
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
                            listener.notifyPictureAndVideoResolutionChanged(type); // waiting
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
                            listener.notifyPictureAndVideoFramerateChanged(framerate); // waiting
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
                    byte massStorageId = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PICTURESETTINGSSTATE_VIDEOAUTORECORDCHANGED_MASS_STORAGE_ID)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyAutoVideoRecordStatusChanged(BooleanExtension.parse(enabled), massStorageId);
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
                            listener.notifyReturnHomeDelayAfterDisconnectChanged(delay);
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
                            listener.notifyHomeTypeChanged(type);
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
                            listener.notifyReturnHomeOnDisconnectStatusChanged(BooleanExtension.parse(state), BooleanExtension.parse(isReadOnly));
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
                            listener.notifyWifiSettingsCountryChanged(codeCountry);
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
                            listener.notifyWifiSettingsAutoCountryStatusChanged(BooleanExtension.parse(automatic));
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
                    ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM keyType = ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM.getFromValue((Integer)args.get(ARFeatureWifi.ARCONTROLLER_DICTIONARY_KEY_WIFI_SECURITYCHANGED_KEY_TYPE));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyWifiSecurityChanged(key, keyType);
                        }
                    });
                }
            }
            /*else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED) && (elementDictionary != null)){
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM state = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED_STATE));
                }
            }
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
            }*/
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#video-stream-state
                // enableVideoStreaming();
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM enabledStatus = ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyVideoStreamingStatusChanged(enabledStatus);
                        }
                    });
                }
            }
            else if (commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_MASSSTORAGECONTENTFORCURRENTRUN){
                // MASS STORAGE CONTENT FOR CURRENT RUN; runId; count pictueres
                // http://developer.parrot.com/docs/reference/bebop_2/#mass-storage-content
                Map<Byte,Short> massContentTypeCount = new HashMap<Byte,Short>();
                if ((elementDictionary != null) && (elementDictionary.size() > 0)) {
                    Iterator<ARControllerArgumentDictionary<Object>> itr = elementDictionary.values().iterator();
                    while (itr.hasNext()) {
                        ARControllerArgumentDictionary<Object> args = itr.next();
                        if (args != null) {
                            byte massStorageId = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_MASSSTORAGECONTENTFORCURRENTRUN_MASS_STORAGE_ID)).intValue();
                            short photoCount = (short)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_MASSSTORAGECONTENTFORCURRENTRUN_NBPHOTOS)).intValue();
                            short videoCount = (short)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_MASSSTORAGECONTENTFORCURRENTRUN_NBVIDEOS)).intValue();
                            short rawPhotoCount = (short)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_MASSSTORAGECONTENTFORCURRENTRUN_NBRAWPHOTOS)).intValue();
                            massContentTypeCount.put(massStorageId, photoCount);
                        }
                    }
                } else {
                    // list is empty
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.notifyMassStorageContentChanged(massContentTypeCount);
                    }
                });
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
                            listener.notifyReturnHomeStateChanged(state, reason);
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
                            //mFlyingState = state;
                            listener.notifyFlyingStateChanged(state);
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
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyPictureStateChanged(state, error);
                        }
                    });
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
                            listener.notifyRecordVideoOrTakePicturesStateChanged(state, error);
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
                            listener.notifyPictureTakenChanged(error);
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
                            //mRunId = runId;
                            listener.notifyCurrentRunIdChanged(runId);
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
                            listener.notifySpeedChanged(speedX, speedY, speedZ);
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
                    byte latitudeAccuracy = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_GPSLOCATIONCHANGED_LATITUDE_ACCURACY)).intValue();
                    byte longitudeAccuracy = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_GPSLOCATIONCHANGED_LONGITUDE_ACCURACY)).intValue();
                    byte altitudeAccuracy = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_GPSLOCATIONCHANGED_ALTITUDE_ACCURACY)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyDroneLocationChanged(new GPSCoordinate(latitude, longitude, altitude), latitudeAccuracy, longitudeAccuracy, altitudeAccuracy);
                        }
                    });
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
                            listener.notifyMaxAltitudeChanged(current, min, max);
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
                            listener.notifyMotorErrorChanged(motorIds, motorError);
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
                            listener.notifyPictureFormatChanged(type);
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
                            listener.notifyWhiteBalanceModeChanged(type);
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
                            listener.notifyPictureIntervalChanged(BooleanExtension.parse(enabled), interval, minInterval, maxInterval);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_GPSFIXSTATECHANGED) && (elementDictionary != null)){
                //setHomeLocation
                // http://developer.parrot.com/docs/reference/bebop_2/#gps-fix-info
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte isFixed = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSETTINGSSTATE_GPSFIXSTATECHANGED_FIXED)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyHomeLocationChanged(BooleanExtension.parse(isFixed));
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORFLIGHTSSTATUSCHANGED) && (elementDictionary != null)){
                // MOTOR FLIGHT STATUS !!! Important!!!
                // http://developer.parrot.com/docs/reference/bebop_2/#motor-flight-status
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    short numberOfFlights = (short)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORFLIGHTSSTATUSCHANGED_NBFLIGHTS)).intValue();
                    short lastFlightDuration = (short)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORFLIGHTSSTATUSCHANGED_LASTFLIGHTDURATION)).intValue();
                    int totalFlightDuration = (int)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORFLIGHTSSTATUSCHANGED_TOTALFLIGHTDURATION);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyMotorFlightStatusChanged(numberOfFlights, lastFlightDuration, totalFlightDuration);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#last-motor-error
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM motorError = ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM.getFromValue((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyLastMotorErrorChanged(motorError);
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_NOFLYOVERMAXDISTANCECHANGED) && (elementDictionary != null)){
                // http://developer.parrot.com/docs/reference/bebop_2/#geofencing
                // IMPORTANT !!!
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte shouldNotFlyOver = (byte)((Integer)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSETTINGSSTATE_NOFLYOVERMAXDISTANCECHANGED_SHOULDNOTFLYOVER)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyGeofencingChanged(BooleanExtension.parse(shouldNotFlyOver));
                        }
                    });
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
                            listener.notifyAltitudeChanged(altitude);
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
                            listener.notifyHomeLocationChanged(new GPSCoordinate(latitude, longitude, altitude));
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
                        final byte state = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_STATE)).intValue();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.notifyFlightPlanComponentStateListChanged(component, BooleanExtension.parse(state));
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
                    final byte availabilityState = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_FLIGHTPLANSTATE_AVAILABILITYSTATECHANGED_AVAILABILITYSTATE)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyFlightPlanAvailabilityStateChanged(BooleanExtension.parse(availabilityState));
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
                            //mAutoFlyingState = state;
                            listener.notifyAutonomousFlightChanged(state, type, filepath);
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
                    //mCalibrationIsRequired = required == 1 ? true:false;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyCalibrationRequiredChanged(BooleanExtension.parse(required));
                        }
                    });
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
                            listener.notifyCalibrationAxisChanged(axis);
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
                            listener.notifyCalibrationStateChanged(BooleanExtension.parse(started));
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATECHANGED) && (elementDictionary != null)){
                // MAGNETO CALIB PROCESS AXIS STATE
                // http://developer.parrot.com/docs/reference/bebop_2/#magneto-calib-process-axis-state
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    byte xAxisIsCalibrated = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATECHANGED_XAXISCALIBRATION)).intValue();
                    byte yAxisIsCalibrated = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATECHANGED_YAXISCALIBRATION)).intValue();
                    byte zAxisIsCalibrated = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATECHANGED_ZAXISCALIBRATION)).intValue();
                    byte calibrationFailed = (byte)((Integer)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATECHANGED_CALIBRATIONFAILED)).intValue();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyCalibrationAxisStateChanged(BooleanExtension.parse(xAxisIsCalibrated),
                                    BooleanExtension.parse(yAxisIsCalibrated),
                                    BooleanExtension.parse(zAxisIsCalibrated),
                                    BooleanExtension.parse(calibrationFailed));
                        }
                    });
                }
            }
            else if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_MAVLINKSTATE_MISSIONITEMEXECUTED) && (elementDictionary != null)){
                // MISSION ITEM EXECUTED
                // http://developer.parrot.com/docs/reference/bebop_2/#mission-item-executed
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    int missionItemId = (int)args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_MAVLINKSTATE_MISSIONITEMEXECUTED_IDX);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyMissionItemExecutedChanged(missionItemId);
                        }
                    });
                }
            }
            else if (commandKey ==  ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSTATE_NUMBEROFSATELLITECHANGED) {
                // http://developer.parrot.com/docs/reference/bebop_2/#number-of-gps-satellites
                ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                if (args != null) {
                    final Integer gpsSatellites = (Integer)  args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_GPSSTATE_NUMBEROFSATELLITECHANGED_NUMBEROFSATELLITE);
                    //Log.d("componentDrone", "gpsSatellite: " + Integer.toString(gpsSatellites));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.notifyNumberOfSatellitesChanged(gpsSatellites);  // show value in TextView
                        }
                    });
                }
            }
        }
    };
}
