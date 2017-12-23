package com.drone.imavis.mvp.util.dronecontroll;

import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;

/**
 * Created by adigu on 23.12.2017.
 */

public interface AutonomousFlightOnCommandReceivedListener {
    void reboot();

    void setMaxDistance();
    void setMaxAltitude();
    void setCurrentDateTime();

    //void setWitheBalanceMode();
    void setPictureInterval();
    void setPictureFormat();
    void recordVideoOrTakePictures();
    void enableVideoStreaming();
    void enableAutoVideoRecord();

    void setWifiSettingsCountry();
    void setWifiSettingsAutoCountry();
    void setWifiSecurity();

    void setHomeLocation();
    void changeHomeLocation(double latitude, double longitude, float altitude, float horizontal_accuracy, float vertical_accuracy, float north_speed, float east_speed, float down_speed, double timestamp);
    void setHomeType();
    void setReturnHomeDelayAfterDisconnect();
    void setReturnHomeOnDisconnect(boolean enable);
    void returnHome();

    void calibration(boolean enable);
    void calibrationAxisX();
    void calibrationAxisY();
    void calibrationAxisZ();

    void cancelMoveTo();

    void connect();
    void disconnect();
    ARCONTROLLER_DEVICE_STATE_ENUM getConnectionState();
    ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM getFlyingState();
    ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM getAutonomousFlyplanState();
    void uploadFlyPlan();

    void pauseAutonomousFlight();
    void stopAutonomousFlight();
    void startAutonomousFlight();

    void land();
}
