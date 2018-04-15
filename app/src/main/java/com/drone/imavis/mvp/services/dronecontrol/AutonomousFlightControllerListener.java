package com.drone.imavis.mvp.services.dronecontrol;

/**
 * Created by adigu on 22.12.2017.
 */

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

import java.util.Date;
import java.util.Map;

/**
 * callbacks with onSuccess and onFailure - https://gist.github.com/cesarferreira/ef70baa8d64f9753b4da
 */
public interface AutonomousFlightControllerListener {

    // media
    void notifyMatchingMediasFoundChanged(int numberOfMedias);
    void notifyDownloadProgressedChanged(String mediaName, int progress);
    void notifyDownloadCompleteChanged(String mediaName);

    // generally infos
    void notifyCurrentDateTimeChanged(Date dateTime); // Date and Time merged
    void notifyConnectionStateChanged(ARCONTROLLER_DEVICE_STATE_ENUM state);
    void notifyBatteryProgressChanged(int batteryProgress);
    void notifyMaxDistanceChanged(float current, float min, float max);
    void notifyMaxAltitudeChanged(float current, float min, float max);
    void notifyNumberOfSatellitesChanged(int statellites);
    void notifySpeedChanged(float speedX, float speedY, float speedZ);
    void notifyAltitudeChanged(double altitude);
    void notifyMoveToChanged(GPSCoordinate location, float heading,
                             ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_ORIENTATION_MODE_ENUM orientationMode,
                             ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_STATUS_ENUM status);

    // Image, Video and Streaming
    void notifyPictureAndVideoSettingsChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED_TYPE_ENUM type,
                                              ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED_FRAMERATE_ENUM framerate);
    void notifyAutoVideoRecordStatusChanged(boolean enabled, byte massStorageId);
    void notifyVideoStreamingStatusChanged(ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM enabledStatus);
    void notifyPictureStateChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM state,
                                   ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM error);
    void notifyRecordVideoOrTakePicturesStateChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM state,
                                                     ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM error);
    void notifyPictureTakenChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error);
    void notifyPictureFormatChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE_ENUM type);
    void notifyPictureIntervalChanged(boolean enabled, float interval, float minInterval, float maxInterval);
    void notifyWhiteBalanceModeChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE_ENUM type);
    // notifyPictureAndVideoResolutionChanged and notifyPictureAndVideoFramerateChanged merged

    // wifi
    void notifyWifiSettingsCountryChanged(String countryCode);
    void notifyWifiSettingsAutoCountryStatusChanged(boolean automatic);
    void notifyWifiSecurityChanged(String key, ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM keyType);

    // Home and ReturnHome
    void notifyReturnHomeDelayAfterDisconnectChanged(short delay);
    void notifyHomeTypeChanged(ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM type);
    void notifyReturnHomeOnDisconnectStatusChanged(boolean enabled, boolean isReadOnly);
    void notifyHomeLocationChanged(boolean isFixed);
    void notifyHomeLocationChanged(GPSCoordinate location);
    void notifyReturnHomeStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE_ENUM state,
                                      ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON_ENUM reason);

    // calibration
    void notifyCalibrationRequiredChanged(boolean isRequired);
    void notifyCalibrationAxisChanged(ARCOMMANDS_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS_ENUM axis);
    void notifyCalibrationStateChanged(boolean started);
    void notifyCalibrationAxisStateChanged(boolean xAxisIsCalibrated, boolean yAxisIsCalibrated,
                                           boolean zAxisIsCalibrated, boolean calibrationFailed);

    // pre fly check
    void notifyFlightPlanComponentStateListChanged(ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM component,
                                                   boolean state);
    void notifyFlightPlanAvailabilityStateChanged(boolean state);
    void notifyAutonomousFlightChanged(ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM state,
                                       ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE_ENUM type,
                                       String filepath);

    // on fly
    void notifyCurrentRunIdChanged(String runId);
    void notifyMassStorageContentChanged(Map<Byte, Short> massContentTypeCount);
    void notifyFlyingStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state);
    void notifyDroneLocationChanged(GPSCoordinate location, byte latitudeAccuracy, byte longitudeAccuracy, byte altitudeAccuracy);
    void notifyMotorFlightStatusChanged(short numberOfFlights, short lastFlightDuration, int totalFlightDuration);
    void notifyGeofencingChanged(boolean shouldNotFlyOver);
    void notifyMissionItemExecutedChanged(int missionItemId);

    // error, failure
    void notifyMotorErrorChanged(byte motorIds, ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM motorError);
    void notifyLastMotorErrorChanged(ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM motorError);
}
