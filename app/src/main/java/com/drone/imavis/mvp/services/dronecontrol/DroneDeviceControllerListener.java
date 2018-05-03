package com.drone.imavis.mvp.services.dronecontrol;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
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

import java.util.Map;

/**
 * Created by adigu on 26.12.2017.
 */

public interface DroneDeviceControllerListener {

    //SDModule
    void notifyMatchingMediasFoundChanged(int numberOfMedias);

    void notifyDownloadProgressedChanged(String mediaName, int progress);

    void notifyDownloadCompleteChanged(String mediaName);

    void notifyConnectionStateChanged(ARCONTROLLER_DEVICE_STATE_ENUM state);

    void notifyBatteryProgressChanged(int batteryProgress);

    void notifyMaxDistanceChanged(float current, float min, float max);

    void notifyMoveToChanged(GPSCoordinate location, float heading,
                             ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_ORIENTATION_MODE_ENUM orientationMode,
                             ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_STATUS_ENUM status);

    void notifyGpsPositionChanged(double latitude, double longtitude, double altitude);

    void notifyCurrentDateChanged(String date);

    void notifyCurrentTimeChanged(String time);

    void notifyPictureAndVideoResolutionChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED_TYPE_ENUM type);

    void notifyPictureAndVideoFramerateChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED_FRAMERATE_ENUM framerate);

    void notifyAutoVideoRecordStatusChanged(boolean enabled, byte massStorageId);

    void notifyReturnHomeDelayAfterDisconnectChanged(short delay);

    void notifyHomeTypeChanged(ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM type);

    void notifyReturnHomeOnDisconnectStatusChanged(boolean enabled, boolean isReadOnly);

    void notifyWifiSettingsCountryChanged(String countryCode);

    void notifyWifiSettingsAutoCountryStatusChanged(boolean automatic);

    void notifyWifiSecurityChanged(String key, ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM keyType);

    void notifyVideoStreamingStatusChanged(ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM enabledStatus);

    void notifyMassStorageContentChanged(Map<Byte, Short> massContentTypeCount);

    void notifyReturnHomeStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE_ENUM state,
                                      ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON_ENUM reason);

    void notifyFlyingStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state);

    void notifyPictureStateChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM state,
                                   ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM error);

    void notifyRecordVideoOrTakePicturesStateChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM state,
                                                     ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM error);

    void notifyPictureTakenChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error);

    void notifyCurrentRunIdChanged(String runId);

    void notifySpeedChanged(float speedX, float speedY, float speedZ);

    void notifyDroneLocationChanged(GPSCoordinate location, byte latitudeAccuracy, byte longitudeAccuracy, byte altitudeAccuracy);

    void notifyMaxAltitudeChanged(float current, float min, float max);

    void notifyMotorErrorChanged(byte motorIds, ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM motorError);

    void notifyPictureFormatChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE_ENUM type);

    void notifyWhiteBalanceModeChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE_ENUM type);

    void notifyPictureIntervalChanged(boolean enabled, float interval, float minInterval, float maxInterval);

    void notifyHomeLocationChanged(boolean isFixed);

    void notifyMotorFlightStatusChanged(short numberOfFlights, short lastFlightDuration, int totalFlightDuration);

    void notifyLastMotorErrorChanged(ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM motorError);

    void notifyGeofencingChanged(boolean shouldNotFlyOver);

    void notifyAltitudeChanged(double altitude);

    void notifyHomeLocationChanged(GPSCoordinate location);

    void notifyFlightPlanComponentStateListChanged(ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM component,
                                                   boolean state);

    void notifyFlightPlanAvailabilityStateChanged(boolean state);

    void notifyAutonomousFlightChanged(ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM state,
                                       ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE_ENUM type,
                                       String filepath);

    void notifyCalibrationRequiredChanged(boolean isRequired);

    void notifyCalibrationAxisChanged(ARCOMMANDS_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS_ENUM axis);

    void notifyCalibrationStateChanged(boolean started);

    void notifyCalibrationAxisStateChanged(boolean xAxisIsCalibrated, boolean yAxisIsCalibrated,
                                           boolean zAxisIsCalibrated, boolean calibrationFailed);

    void notifyMissionItemExecutedChanged(int missionItemId);

    void notifyNumberOfSatellitesChanged(int statellites);
}
