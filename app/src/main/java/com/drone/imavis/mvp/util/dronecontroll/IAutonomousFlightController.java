package com.drone.imavis.mvp.util.dronecontroll;

import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEOV2_RECORD_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIASTREAMING_VIDEOSTREAMMODE_MODE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_AUTOWHITEBALANCESELECTION_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_PICTUREFORMATSELECTION_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEOFRAMERATE_FRAMERATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEORESOLUTIONS_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_WIFI_SECURITY_KEY_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;

import java.util.Date;

/**
 * Created by adigu on 23.12.2017.
 */

public interface IAutonomousFlightController {

    void setMaxDistance(float maxDistance);
    void setMaxAltitude(float maxAltitude);
    void setCurrentDateTime(Date dateTime);

    //void setWitheBalanceMode(ARCOMMANDS_ARDRONE3_PICTURESETTINGS_AUTOWHITEBALANCESELECTION_TYPE_ENUM type);
    void setStreamingMode(ARCOMMANDS_ARDRONE3_MEDIASTREAMING_VIDEOSTREAMMODE_MODE_ENUM mode);
    void setPictureAndVideoSettings(ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEORESOLUTIONS_TYPE_ENUM type,
                                    ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEOFRAMERATE_FRAMERATE_ENUM frameRate);
    void setPictureInterval(boolean enable, float interval);
    void setPictureFormat(ARCOMMANDS_ARDRONE3_PICTURESETTINGS_PICTUREFORMATSELECTION_TYPE_ENUM type);
    void recordVideoOrTakePictures(ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEOV2_RECORD_ENUM record, int type, float interval);
    void enableVideoStreaming(boolean enable);
    void enableAutoVideoRecord(boolean enableAutoRecord, byte autoRecordMassStorageId);


    void setWifiSettingsCountry(String countryCode);
    void setWifiSettingsAutoCountry(boolean automatic);
    void setWifiSecurity(ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM type, String key, ARCOMMANDS_WIFI_SECURITY_KEY_TYPE_ENUM keyType);

    void setHomeType(ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM type);
    void setHomeLocation(GPSCoordinate location);
    void changeHomeLocation(GPSCoordinate location, float horizontalAccuracy, float verticalAccuracy, float northSpeed, float eastSpeed, float downSpeed, double timestamp);
    void setReturnHomeOnDisconnect(boolean enable);
    void setReturnHomeDelayAfterDisconnect(short delayInSecond);
    void returnHome();

    void calibration(boolean start);
    boolean isCalibrationRequired();
    //void calibrationAxisX();
    //void calibrationAxisY();
    //void calibrationAxisZ();

    void addListener(AutonomousFlightControllerListener listener);
    void removeListener(AutonomousFlightControllerListener listener);
    void dispose();
    boolean connect();
    boolean disconnect();
    void reboot();

    void uploadAutonomousFlightPlan(FlyPlan flyPlan, String localFilepath);
    void startAutonomousFlight();
    void pauseAutonomousFlight();
    void stopAutonomousFlight();
    void cancelMoveTo();
    void land();

    void getLastFlightMedias();
    int getBatteryStatus();
    String getRunId();

    ARCONTROLLER_DEVICE_STATE_ENUM getConnectionState();
    ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM getFlyingState();
    ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM getFlyPlanState();
}
