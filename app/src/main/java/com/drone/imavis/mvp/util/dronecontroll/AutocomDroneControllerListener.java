package com.drone.imavis.mvp.util.dronecontroll;

/**
 * Created by adigu on 22.12.2017.
 */

import com.drone.imavis.mvp.util.listener.OnEventListener;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerCodec;
import com.parrot.arsdk.arcontroller.ARFrame;

/**
 * callbacks with onSuccess and onFailure - https://gist.github.com/cesarferreira/ef70baa8d64f9753b4da
 */
public interface AutocomDroneControllerListener {

    // Monitoring - States
    void onDroneConnectionChanged(ARCONTROLLER_DEVICE_STATE_ENUM state);
    void onBatteryChargeChanged(int batteryPercentage);
    void onPilotingStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state);
    void onGPSChange(double latitude, double longtitude, double altitude);
    void onSpeedChange(float north, float east, float down);
    void onAltitudeChange(double altitude);
    void onAutoStateChanged(ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM state);
    void onAutoAvailableState(byte availabilityState);
    void numberOfSatellites(Integer gpsSatellite);

    // configuration
    void onCalibration();
    void configureDecoder(ARControllerCodec codec);

    // Fly
    //void onHome();
    //void onTakeOff();
    void onCancelMoveTo();
    void onAutoFlight();
    void onHomeReturn(double latitude, double longtitude, double altitude);
    void onFlightPath(ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM component, byte State);
    void onUploadFlyPlan();
    void onPauseMavLinkFlyPlan();
    void onStopMavLinkFlyPlan();
    void onLand();
    void onEmergency();

    // Picture - Video - Media
    // void onTakePicture();
    void onPictureTaken(ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error);
    void onFrameReceived(ARFrame frame);
    void onMatchingMediasFound(int nbMedias);
    void onDownloadProgressed(String mediaName, int progress);
    void onDownloadComplete(String mediaName);
    void onGetLastFlightMedias();
    void onCancelGetLastFlightMedias();
    void onStartTakingVideoOrPictures();
    void onEnableVideoStreaming();

    // SET
    void onSetMaxDistanceChange(OnEventListener<Object,Object> listener);
    void onSetPictureFormatChange();
    //void onSetWitheBalanceModeChange();
    void onSetTakePeriodicalPictureChange();
    void onSetStreamingSettingsChange();
    void onSetAutoReturnHomeDelayChange();
    void onSetHomeLocationChange();
    void onSetMaxAltitudeChange();
    void onSetReturnHomeOnDisconnectFlyPlanChange();
}
