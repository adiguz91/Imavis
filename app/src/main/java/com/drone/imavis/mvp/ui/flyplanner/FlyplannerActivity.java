package com.drone.imavis.mvp.ui.flyplanner;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Path;
import android.location.Location;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.local.preference.PreferencesHelper;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.dronecontrol.AutonomousFlightController;
import com.drone.imavis.mvp.services.dronecontrol.AutonomousFlightControllerListener;
import com.drone.imavis.mvp.services.dronecontrol.DronePermissionRequestHelper;
import com.drone.imavis.mvp.services.dronecontrol.MavlinkFileInfo;
import com.drone.imavis.mvp.services.dronecontrol.bebopexamples.DroneDiscoverer;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.services.flyplan.mvc.view.SheetFab;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.FlyplannerFragment;
import com.drone.imavis.mvp.ui.searchwlan.SearchWlanActivity;
import com.drone.imavis.mvp.util.DialogUtil;
import com.drone.imavis.mvp.util.IWifiUtilCallback;
import com.drone.imavis.mvp.util.LoadingDialogUtil;
import com.drone.imavis.mvp.util.StringUtil;
import com.drone.imavis.mvp.util.UnsubscribeIfPresent;
import com.drone.imavis.mvp.util.WifiUtil;
import com.github.clans.fab.FloatingActionMenu;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEOV2_RECORD_ENUM;
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
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;
import com.parrot.arsdk.arutils.ARUtilsException;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.thanosfisherman.wifiutils.WifiUtils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

/**
 * Created by adigu on 30.05.2017.
 */

public class FlyplannerActivity extends BaseActivity implements IFlyplannerActivity, DroneDiscoverer.Listener { // FragmentActivity

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity.EXTRA_TRIGGER_SYNC_FLAG";
    /**
     * List of runtime permission we need.
     */
    private static final String[] PERMISSIONS_NEEDED = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    /**
     * Code for permission request result handling.
     */
    private static final int REQUEST_CODE_PERMISSIONS_REQUEST = 1;
    private static final int ANIMATION_DURATION = 300;
    private static final float ROTATION_ANGLE = -45f;

    // this block loads the native libraries
    // it is mandatory
    static {
        ARSDK.loadSDKLibs();
    }

    public DroneDiscoverer droneDiscoverer;
    @Inject
    LoadingDialogUtil loadingDialogUtil;
    @Inject
    FlyplannerPresenter flyplannerPresenter;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    WifiUtil wifiUtil;
    Context context;
    @Inject
    DronePermissionRequestHelper dronePermissionRequestHelper;
    @Inject
    DialogUtil dialogUtil;
    List<ARDiscoveryDeviceService> dronesList;
    ARDiscoveryDeviceService drone;
    @BindView(R.id.flyplanner_fab_pc_start)
    FABProgressCircle fabProgressCircleStart;
    @BindView(R.id.batteryLevel)
    TextView batteryLevel;
    // header
    @BindView(R.id.flyplanner_fab_back)
    FloatingActionButton fabBack;
    @BindView(R.id.flyplanner_fab_droneCalibration)
    FloatingActionButton fabDroneCalibration;
    @BindView(R.id.flyplanner_fab_currentGpsPosition)
    FloatingActionButton fabCurrentGpsPosition;
    @BindView(R.id.flyplanner_fab_droneConnectWifi)
    FloatingActionButton fabDroneConnectWifi;
    @BindView(R.id.flyplanner_fab_flyplanSettings)
    FloatingActionButton fabFlyplanSettings;
    @BindView(R.id.flyplanner_fab_mapLock)
    FloatingActionButton fabMapLock;
    @BindView(R.id.flyplanner_fab_mapType_menu)
    FloatingActionMenu fabMapTypeMenu;
    @BindView(R.id.flyplanner_fab_mapType_menu_terrain)
    com.github.clans.fab.FloatingActionButton fabMapTypeTerrain;
    @BindView(R.id.flyplanner_fab_mapType_menu_satellite)
    com.github.clans.fab.FloatingActionButton fabMapTypeSatellite;
    @BindView(R.id.flyplanner_fab_start)
    FloatingActionButton fabStart;
    private FlyPlan flyplan;
    private AutonomousFlightController autonomController;
    private GPSCoordinate currentDronePosition;
    private float beforeTakeOffElevation;
    private MaterialSheetFab actionFabSheetMenu;
    private SheetFab fabSheet;
    private FlyplannerFragment flyplannerFragment;
    private ReactiveLocationProvider locationProvider;
    private Observable<Location> lastKnownLocationObservable;
    private Disposable lastKnownLocationDisposable;
    private boolean mapIsLocked = false;
    private View sheetView;
    private boolean isFlyplanStarted = false;
    private IWifiUtilCallback wifiUtilCallback = new IWifiUtilCallback() {
        @Override
        public void onSuccess() {
            showToast("Connection succeeded!");
        }

        @Override
        public void onFail() {
            showToast("Connection failed!");
        }
    };
    private AnimatorSet mOpenAnimatorSet;
    private AnimatorSet mCloseAnimatorSet;
    private String lasKnownSSID = "BebopDrone-C074449";
    private int RESULT_BACK_PRESSED = 2000;
    private AutonomousFlightControllerListener autonomousFlightControllerListener = new AutonomousFlightControllerListener() {
        @Override
        public void notifyMatchingMediasFoundChanged(int numberOfMedias) {
            Log.d("AFCL", "notifyMatchingMediasFoundChanged");

        }

        @Override
        public void notifyDownloadProgressedChanged(String mediaName, int progress) {
            Log.d("AFCL", "notifyDownloadProgressedChanged");
        }

        @Override
        public void notifyDownloadCompleteChanged(String mediaName) {
            Log.d("AFCL", "notifyDownloadCompleteChanged");
        }

        @Override
        public void notifyCurrentDateTimeChanged(Date dateTime) {
            Log.d("AFCL", "notifyCurrentDateTimeChanged");
        }

        @Override
        public void notifyConnectionStateChanged(ARCONTROLLER_DEVICE_STATE_ENUM state) {
            Log.d("AFCL", "notifyConnectionStateChanged");
        }

        @Override
        public void notifyBatteryProgressChanged(int batteryProgress) {
            Log.d("AFCL", "notifyBatteryProgressChanged " + batteryProgress);
            changeBatteryLevel(batteryProgress);
        }

        @Override
        public void notifyMaxDistanceChanged(float current, float min, float max) {
            Log.d("AFCL", "notifyMaxDistanceChanged");
        }

        @Override
        public void notifyMaxAltitudeChanged(float current, float min, float max) {
            Log.d("AFCL", "notifyMaxAltitudeChanged: min-" + min + " max-" + max + " current-" + current);
            beforeTakeOffElevation = current;
        }

        @Override
        public void notifyNumberOfSatellitesChanged(int statellites) {
            Log.d("AFCL", "notifyNumberOfSatellitesChanged: " + statellites);
        }

        @Override
        public void notifySpeedChanged(float speedX, float speedY, float speedZ) {
            //Log.d("AFCL", "notifySpeedChanged");
        }

        @Override
        public void notifyAltitudeChanged(double altitude) {
            //Log.d("AFCL", "notifyAltitudeChanged");
        }

        @Override
        public void notifyMoveToChanged(GPSCoordinate location, float heading, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_ORIENTATION_MODE_ENUM orientationMode, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_MOVETOCHANGED_STATUS_ENUM status) {
            Log.d("AFCL", "notifyMoveToChanged");
        }

        @Override
        public void notifyGpsPositionChanged(GPSCoordinate location) {
            Log.d("AFCL", "notifyGpsPositionChanged");
            currentDronePosition = location;
        }

        @Override
        public void notifyPictureAndVideoSettingsChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEORESOLUTIONSCHANGED_TYPE_ENUM type, ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOFRAMERATECHANGED_FRAMERATE_ENUM framerate) {
            Log.d("AFCL", "notifyPictureAndVideoSettingsChanged");
        }

        @Override
        public void notifyAutoVideoRecordStatusChanged(boolean enabled, byte massStorageId) {
            Log.d("AFCL", "notifyAutoVideoRecordStatusChanged");
        }

        @Override
        public void notifyVideoStreamingStatusChanged(ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM enabledStatus) {
            Log.d("AFCL", "notifyVideoStreamingStatusChanged");
        }

        @Override
        public void notifyPictureStateChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM state, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM error) {
            Log.d("AFCL", "notifyPictureStateChanged");
        }

        @Override
        public void notifyRecordVideoOrTakePicturesStateChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM state, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM error) {
            Log.d("AFCL", "notifyRecordVideoOrTakePicturesStateChanged");
        }

        @Override
        public void notifyPictureTakenChanged(ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error) {
            Log.d("AFCL", "notifyPictureTakenChanged");
        }

        @Override
        public void notifyPictureFormatChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE_ENUM type) {
            Log.d("AFCL", "notifyPictureFormatChanged");
        }

        @Override
        public void notifyPictureIntervalChanged(boolean enabled, float interval, float minInterval, float maxInterval) {
            Log.d("AFCL", "notifyPictureIntervalChanged");
        }

        @Override
        public void notifyWhiteBalanceModeChanged(ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE_ENUM type) {
            Log.d("AFCL", "notifyWhiteBalanceModeChanged");
        }

        @Override
        public void notifyWifiSettingsCountryChanged(String countryCode) {
            Log.d("AFCL", "notifyWifiSettingsCountryChanged");
        }

        @Override
        public void notifyWifiSettingsAutoCountryStatusChanged(boolean automatic) {
            Log.d("AFCL", "notifyWifiSettingsAutoCountryStatusChanged");
        }

        @Override
        public void notifyWifiSecurityChanged(String key, ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM keyType) {
            Log.d("AFCL", "notifyWifiSecurityChanged");
        }

        @Override
        public void notifyReturnHomeDelayAfterDisconnectChanged(short delay) {
            Log.d("AFCL", "notifyReturnHomeDelayAfterDisconnectChanged");
        }

        @Override
        public void notifyHomeTypeChanged(ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM type) {
            Log.d("AFCL", "notifyHomeTypeChanged: " + type.toString());
        }

        @Override
        public void notifyReturnHomeOnDisconnectStatusChanged(boolean enabled, boolean isReadOnly) {
            Log.d("AFCL", "notifyReturnHomeOnDisconnectStatusChanged");
        }

        @Override
        public void notifyHomeLocationChanged(boolean isFixed) {
            Log.d("AFCL", "notifyHomeLocationChanged: " + isFixed);
        }

        @Override
        public void notifyHomeLocationChanged(GPSCoordinate location) {
            Log.d("AFCL", "notifyHomeLocationChanged");
        }

        @Override
        public void notifyReturnHomeStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE_ENUM state, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON_ENUM reason) {
            Log.d("AFCL", "notifyReturnHomeStateChanged");
        }

        @Override
        public void notifyCalibrationRequiredChanged(boolean isRequired) {
            Log.d("AFCL", "notifyCalibrationRequiredChanged: " + isRequired);
        }

        @Override
        public void notifyCalibrationAxisChanged(ARCOMMANDS_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS_ENUM axis) {
            Log.d("AFCL", "notifyCalibrationAxisChanged: " + axis.toString());
        }

        @Override
        public void notifyCalibrationStateChanged(boolean started) {
            Log.d("AFCL", "notifyCalibrationStateChanged: " + started);
        }

        @Override
        public void notifyCalibrationAxisStateChanged(boolean xAxisIsCalibrated, boolean yAxisIsCalibrated, boolean zAxisIsCalibrated, boolean calibrationFailed) {
            Log.d("AFCL", "notifyCalibrationAxisStateChanged xAxisIsCalibrated: " + xAxisIsCalibrated);
            Log.d("AFCL", "notifyCalibrationAxisStateChanged yAxisIsCalibrated: " + yAxisIsCalibrated);
            Log.d("AFCL", "notifyCalibrationAxisStateChanged zAxisIsCalibrated: " + zAxisIsCalibrated);
            Log.d("AFCL", "notifyCalibrationAxisStateChanged calibrationFailed: " + calibrationFailed);
        }

        @Override
        public void notifyFlightPlanComponentStateListChanged(ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM component, boolean state) {
            Log.d("AFCL", "notifyFlightPlanComponentStateListChanged: " + component.toString() + "; state: " + state);
        }

        @Override
        public void notifyFlightPlanAvailabilityStateChanged(boolean state) {
            Log.d("AFCL", "notifyFlightPlanAvailabilityStateChanged: " + state);
        }

        @Override
        public void notifyAutonomousFlightChanged(ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM state, ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE_ENUM type, String filepath) {
            Log.d("AFCL", "notifyAutonomousFlightChanged STATE: " + state.toString());
            Log.d("AFCL", "notifyAutonomousFlightChanged TYPE: " + type.toString());
            Log.d("AFCL", "notifyAutonomousFlightChanged filepath: " + filepath);
        }

        @Override
        public void notifyCurrentRunIdChanged(String runId) {
            Log.d("AFCL", "notifyCurrentRunIdChanged: " + runId);
        }

        @Override
        public void notifyMassStorageContentChanged(Map<Byte, Short> massContentTypeCount) {
            Log.d("AFCL", "notifyMassStorageContentChanged: ");
        }

        @Override
        public void notifyFlyingStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state) {
            Log.d("AFCL", "notifyFlyingStateChanged: " + state.toString());
        }

        @Override
        public void notifyDroneLocationChanged(GPSCoordinate location, byte latitudeAccuracy, byte longitudeAccuracy, byte altitudeAccuracy) {
            Log.d("AFCL", "notifyDroneLocationChanged");
        }

        @Override
        public void notifyMotorFlightStatusChanged(short numberOfFlights, short lastFlightDuration, int totalFlightDuration) {
            Log.d("AFCL", "notifyMotorFlightStatusChanged");
        }

        @Override
        public void notifyGeofencingChanged(boolean shouldNotFlyOver) {
            Log.d("AFCL", "notifyGeofencingChanged");
        }

        @Override
        public void notifyMissionItemExecutedChanged(int missionItemId) {
            Log.d("AFCL", "notifyMissionItemExecutedChanged: " + missionItemId);
        }

        @Override
        public void notifyMotorErrorChanged(byte motorIds, ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM motorError) {
            Log.d("AFCL", "notifyMotorErrorChanged: " + motorError.toString());
        }

        @Override
        public void notifyLastMotorErrorChanged(ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM motorError) {
            Log.d("AFCL", "notifyLastMotorErrorChanged: " + motorError.toString());
        }
    };

    public static String combine(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }

    // This snippet hides the system bars.
    private void hideStatusBar() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public LoadingDialogUtil getLoadingDialog() {
        return loadingDialogUtil;
    }

    private void showToast(String message) {
        Toast toast = new Toast(this);
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    // TODO : https://github.com/mcharmas/Android-ReactiveLocation/blob/master/sample/src/main/java/pl/charmas/android/reactivelocation2/sample/BaseActivity.java
    //protected abstract void onLocationPermissionGranted();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        getSupportActionBar().hide();
        //hideStatusBar();
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A000000")));
        //getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A000000")));

        flyplan = getIntent().getParcelableExtra("Flyplan");
        if (flyplan == null)
            flyplan = new FlyPlan();

        setContentView(R.layout.activity_flyplanner);
        ButterKnife.bind(this);

        setHeader();
        setUpCustomFabMenuAnimation();
        if (fabMapTypeMenu.isOpened()) {
            fabMapTypeMenu.setClickable(true);
        } else {
            fabMapTypeMenu.setClickable(false);
        }

        flyplannerFragment = (FlyplannerFragment) getSupportFragmentManager().findFragmentById(R.id.flyplanner);
        context = this;

        /* Drone */
        droneDiscoverer = new DroneDiscoverer(this);
        dronePermissionRequestHelper.requestPermission(PERMISSIONS_NEEDED, REQUEST_CODE_PERMISSIONS_REQUEST);

        flyplannerPresenter.attachView(this);


        //getActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fabSheet = findViewById(R.id.fabSheet);
        sheetView = findViewById(R.id.fabSheetCardView);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.white);
        int fabColor = getResources().getColor(R.color.accent_color);

        fabSheet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
        fabSheet.setRippleColor(getResources().getColor(R.color.transparent));
        fabSheet.setElevation(0);
        fabSheet.setCompatElevation(0);

        // Initialize material sheet FAB
        actionFabSheetMenu = new MaterialSheetFab<>(fabSheet, sheetView, overlay,
                sheetColor, fabColor);

        actionFabSheetMenu.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Called when the material sheet's "show" animation starts.
            }

            @Override
            public void onSheetShown() {
                // Called when the material sheet's "show" animation ends.
            }

            @Override
            public void onHideSheet() {
                // Called when the material sheet's "hide" animation starts.
            }

            public void onSheetHidden() {
                // Called when the material sheet's "hide" animation ends.
                fabSheet.setVisibility(View.GONE);
            }
        });

        fabProgressCircleStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                // todo start the drone to fly
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        fabProgressCircleStart.beginFinalAnimation();
                    }
                }, 5 * 1000);*/
            }
        });

        //FloatingActionButton fabStart = (FloatingActionButton) findViewById(R.id.flyplanner_fab_start);
        fabStart.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_play)
                .colorRes(R.color.icons)
                .actionBarSize());

        fabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isFlyplanStarted) {
                    fabProgressCircleStart.show();
                    // change icon to "x" cancel
                    fabStart.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_times)
                            .colorRes(R.color.icons)
                            .actionBarSize());

                    long duration = 10000; // calculate duration alonge
                    ImageView imageView = findViewById(R.id.droneFlyingState);
                    moveViewAlongPath(imageView, flyplan.getPathRoute(imageView.getWidth() / 2, imageView.getHeight() / 2), duration);

                    if (autonomController != null) {
                        flyplannerFragment.getGoogleMapFragment(); //?

                        ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM homeType = ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM.ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_TAKEOFF;
                        autonomController.setHomeType(homeType);

                        lastKnownLocationObservable.subscribe(x -> {
                            GPSCoordinate homeLocation = new GPSCoordinate(x.getLatitude(), x.getLongitude(), x.getAltitude());
                            autonomController.setHomeLocation(homeLocation);
                        });

                        //for test purpose
                        autonomController.setMaxAltitude(2);

                        float maxAltitude = 2; // current + offset; //autonomController.setMaxAltitude(maxAltitude);
                        //String localFilepath = autonomController.generateMavlinkFile(flyplan.getPoints(), (short)3); // alt 516
                        MavlinkFileInfo mavlinkFileInfo = autonomController.generateMavlinkFileTest(currentDronePosition, (short) 3, maxAltitude);
                        autonomController.uploadAutonomousFlightPlan(flyplan, mavlinkFileInfo.getFilePath());
                        try {
                            Thread.sleep(2000);
                            float pictureInterval = 4;
                            autonomController.recordVideoOrTakePictures(ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEOV2_RECORD_ENUM.ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEOV2_RECORD_START, 1, pictureInterval);
                            autonomController.startAutonomousFlight(); // "flightPlan.mavlink" maxAltitude
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // TODO shape design pattern
                        // https://www.tutorialspoint.com/design_pattern/decorator_pattern.htm

                        //TODO: if finished then upload images
                        flyplannerPresenter.startFlyplanTask(flyplan);
                    }
                    isFlyplanStarted = true;
                } else {
                    fabProgressCircleStart.hide();
                    // cancel mode
                    if (autonomController != null) {
                        autonomController.stopAutonomousFlight();
                        autonomController.returnHome();
                    }

                    fabStart.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_play)
                            .colorRes(R.color.icons)
                            .actionBarSize());
                    isFlyplanStarted = false;
                }

            }
        });

        fabProgressCircleStart.attachListener(new FABProgressListener() {
            @Override
            public void onFABProgressAnimationEnd() {
                Toast.makeText(FlyplannerActivity.this, "Finished Drone", Toast.LENGTH_LONG).show();
            }
        });

        initLocation();

        wifiUtil.setWifiUtilCallback(wifiUtilCallback);

        if (StringUtil.isNullOrEmpty(preferencesHelper.getDroneWifiSsid()))
            findSsid(preferencesHelper.getDroneWifiSsid());

        // todo: init flyplanner fragment instance

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }
    }

    private void findSsid(String ssid) {
        WifiUtils.withContext(context).enableWifi(isSuccess -> {
            if (isSuccess) {
                WifiUtils.withContext(context).scanWifi(scanResults -> {
                    for (ScanResult scanResult : scanResults) {
                        if (scanResult.equals(ssid)) {
                            // found
                            wifiUtil.showDialogConnect(scanResult);
                            break;
                        }
                    }
                }).start();
            } else {
                // not found
            }
        });
    }

    //-------------------

    public MaterialSheetFab getActionFabSheetMenu() {
        return actionFabSheetMenu;
    }

    public SheetFab getActionFabSheet() {
        return fabSheet;
    }

    public View getActionFabSheetCardView() {
        return sheetView;
    }

    public void setHeader() {

        fabMapTypeMenu.getMenuIconView().setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_map)
                .colorRes(R.color.icons)
                .actionBarSize());

        fabBack.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_arrow_left)
                .colorRes(R.color.icons)
                .actionBarSize());

        fabCurrentGpsPosition.setImageDrawable(
                new IconDrawable(this, FontAwesomeIcons.fa_crosshairs)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        Icon lockingIcon = FontAwesomeIcons.fa_unlock_alt;
        int ressourceColor = R.color.md_yellow_400;
        if (mapIsLocked) {
            lockingIcon = FontAwesomeIcons.fa_lock;
            ressourceColor = R.color.md_red_400;
        }

        fabMapLock.setImageDrawable(new IconDrawable(this, lockingIcon)
                .colorRes(ressourceColor)
                .actionBarSize());

        int wifiColor = R.color.md_red_400;
        if (isWifiDroneConnectionActive(lasKnownSSID)) {
            wifiColor = R.color.md_green_400;
        }

        fabDroneConnectWifi.setImageDrawable(
                new IconDrawable(this, FontAwesomeIcons.fa_wifi)
                        .colorRes(wifiColor)
                        .actionBarSize());

        fabFlyplanSettings.setImageDrawable(
                new IconDrawable(this, FontAwesomeIcons.fa_cog)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        ViewTooltip.on(this, fabDroneCalibration)
                .corner(30)
                .autoHide(false, 1000)
                .position(ViewTooltip.Position.BOTTOM)
                .text("Calibrate!")
                .clickToHide(false)
                .textColor(Color.WHITE)
                .color(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    private void connectToDrone() {
        String serviceName = lasKnownSSID; // SSID wlan of the drone
        if (dronesList != null) {
            for (ARDiscoveryDeviceService droneService : dronesList) {
                if (droneService.getName().equals(serviceName)) {
                    drone = droneService;
                    break;
                }
            }
            if (drone != null) {
                autonomController = null;
                try {
                    autonomController = new AutonomousFlightController(context, drone);
                } catch (ARUtilsException e) {
                    e.printStackTrace();
                }

                autonomController.setListener(autonomousFlightControllerListener);
                autonomController.getConnectionState();
                autonomController.connect();
            }
        }
    }

    public FlyPlan getFlyplan() {
        return flyplan;
    }

    public void onLocationPermissionGranted() {
        // https://github.com/mcharmas/Android-ReactiveLocation
        /*lastKnownLocationDisposable = lastKnownLocationObservable.
                .map(new LocationToStringFunc())
                .subscribe(new DisplayTextOnViewAction(lastKnownLocationView), new ErrorHandler());*/

        lastKnownLocationObservable
                .subscribe(x ->
                        Log.d("location", x.toString()));
    }

    public void initLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);

        locationProvider = new ReactiveLocationProvider(getApplicationContext());

        lastKnownLocationObservable = locationProvider
                .getLastKnownLocation()
                .observeOn(AndroidSchedulers.mainThread());

        // TODO: move this rxPermissions to application start init, and into dagger
        // https://github.com/tbruyelle/RxPermissions/blob/521654fb8ffc69b32faba70523ba436689f6c195/sample/src/main/java/com/tbruyelle/rxpermissions/sample/MainActivity.java
        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
        rxPermissions.setLogging(true);

        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            onLocationPermissionGranted();
                        } else {
                            //Toast.makeText(BaseActivity.this, "Sorry, no demo without permission...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setUpCustomFabMenuAnimation() {
        mOpenAnimatorSet = new AnimatorSet();
        mCloseAnimatorSet = new AnimatorSet();

        ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(fabMapTypeMenu.getMenuIconView(),
                "rotation",
                -90f + ROTATION_ANGLE, 0f);
        ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(fabMapTypeMenu.getMenuIconView(),
                "rotation",
                0f, -90f + ROTATION_ANGLE);

        expandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // this will be rotated so that the plus icon will be seen as "x"
                fabMapTypeMenu.getMenuIconView().setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_plus)
                        .colorRes(R.color.icons)
                        .actionBarSize());
                fabMapTypeMenu.setIconToggleAnimatorSet(mCloseAnimatorSet);
                fabMapTypeMenu.setClickable(true);
            }
        });

        collapseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fabMapTypeMenu.getMenuIconView().setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_map)
                        .colorRes(R.color.icons)
                        .actionBarSize());
                fabMapTypeMenu.setIconToggleAnimatorSet(mOpenAnimatorSet);
                fabMapTypeMenu.setClickable(false);
            }
        });

        mOpenAnimatorSet.play(expandAnimator);
        mCloseAnimatorSet.play(collapseAnimator);

        mOpenAnimatorSet.setDuration(ANIMATION_DURATION);
        mCloseAnimatorSet.setDuration(ANIMATION_DURATION);

        fabMapTypeMenu.setIconToggleAnimatorSet(mOpenAnimatorSet);
    }

    private void changeBatteryLevel(int percentage) {

        batteryLevel.setText(percentage + "%");
        if (percentage >= 75) {
            // green
            batteryLevel.setTextColor(getResources().getColor(R.color.white));
            batteryLevel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_green_700)));
        } else if (75 > percentage && percentage >= 50) {
            // yellow
            batteryLevel.setTextColor(getResources().getColor(R.color.md_black_1000));
            batteryLevel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_yellow_600)));
        } else if (50 > percentage && percentage >= 25) {
            // orange
            batteryLevel.setTextColor(getResources().getColor(R.color.white));
            batteryLevel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_orange_800)));
        } else if (25 > percentage) {
            //red
            batteryLevel.setTextColor(getResources().getColor(R.color.white));
            batteryLevel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_red_800)));
        }
    }

    private boolean isWifiDroneConnectionActive(String checkSsid) {
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED) {
            String droneSsid = wifiInfo.getSSID();
            return droneSsid.equals(checkSsid);
        }
        return false;
    }

    @OnClick(R.id.flyplanner_fab_mapLock)
    public void onFabClickMapLock(FloatingActionButton button) {
        mapIsLocked = !mapIsLocked;

        FlyPlanView flyplannerDrawer = ((Activity) context).findViewById(R.id.flyplannerDraw);
        if (flyplannerDrawer != null) {
            FlyPlanView.setIsLocked(mapIsLocked);
        }

        Icon lockingIcon = FontAwesomeIcons.fa_unlock_alt;
        int ressourceColor = R.color.md_yellow_400;
        if (mapIsLocked) {
            lockingIcon = FontAwesomeIcons.fa_lock;
            ressourceColor = R.color.md_red_400;
        }
        fabMapLock.setImageDrawable(new IconDrawable(this, lockingIcon)
                .colorRes(ressourceColor)
                .actionBarSize());
    }

    @OnClick(R.id.flyplanner_fab_droneConnectWifi)
    public void onFabClickDroneConnectWifi(FloatingActionButton button) {
        goToActivity(this, SearchWlanActivity.class, new Bundle());
    }

    @OnClick(R.id.flyplanner_fab_droneCalibration)
    public void onFabClickDroneCalibration(FloatingActionButton button) {
        // TODO start drone calibration
    }

    @OnClick(R.id.flyplanner_fab_back)
    public void onFabClickBack(FloatingActionButton button) {
        Intent intent = new Intent();
        intent.putExtra("Flyplan", flyplan);
        setResult(RESULT_BACK_PRESSED, intent);
        //super.onBackPressed();
        finish();
    }

    @OnClick(R.id.flyplanner_fab_mapType_menu)
    public void onFabClickMapType(FloatingActionMenu button) {
        //goToActivity(this, ModelViewerActivity.class, new Bundle());
    }

    @OnClick(R.id.flyplanner_fab_mapType_menu_satellite)
    public void onMapTypeSatelliteClicked(com.github.clans.fab.FloatingActionButton button) {
        flyplannerFragment.getGoogleMapFragment().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        fabMapTypeMenu.close(true);
    }

    @OnClick(R.id.flyplanner_fab_mapType_menu_terrain)
    public void onMapTypeTerrainClicked(com.github.clans.fab.FloatingActionButton button) {
        flyplannerFragment.getGoogleMapFragment().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        fabMapTypeMenu.close(true);
    }

    @OnClick(R.id.flyplanner_fab_currentGpsPosition)
    public void onFabClickCurrentGpsPosition(FloatingActionButton button) {
        lastKnownLocationObservable.subscribe(x -> {
            //GPSCoordinate homeLocation = new GPSCoordinate(x.getLatitude(), x.getLongitude(), x.getAltitude());
            LatLng location = new LatLng(x.getLatitude(), x.getLongitude());
            flyplannerFragment.getGoogleMapFragment().setMarker(location);
        });
    }

    private void moveViewAlongPath(final View view, final Path path, long duration) {
        if (path == null || path.isEmpty())
            return;

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        objectAnimator.setDuration(duration);
        objectAnimator.setAutoCancel(true);
        objectAnimator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //loginPresenter.detachView();
    }

    @Override
    public void onSaveFlyplanSuccess(FlyPlan flyplan) {

    }

    @Override
    public void onSaveFlyplanFailed() {

    }

    @Override
    public void onStartFlyplanTaskSuccess(FlyPlan flyplan) {

    }

    @Override
    public void onStartFlyplanTaskFailed() {

    }

    @Override
    public void updateFlyplanNodes(List<GPSCoordinate> waypointGpsCoordinates, List<GPSCoordinate> poiGpsCoordinates) {
        // TODO set GPS data to flyplan and save to db!
        Toast.makeText(this, "The camera has stopped moving.",
                Toast.LENGTH_SHORT).show();
    }

    /*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
    */

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //boolean result = super.onTouchEvent(event);
        return false; //!result;
    }
    */

    /* Drone Discovery Code */

    @Override
    public void onPause() {
        super.onPause();
        flyplannerPresenter.saveFlyplan(flyplan);

        // clean the drone discoverer object
        droneDiscoverer.stopDiscovering();
        //mDroneDiscoverer.cleanup();
        //mDroneDiscoverer.removeListener(mDiscovererListener);
    }

    @Override
    public void onDronesListUpdated(List<ARDiscoveryDeviceService> dronesList) {

        if (dronesList == null || dronesList.isEmpty()) {
            Toast.makeText(this, "No Drones found", Toast.LENGTH_SHORT).show();
            return;
        }
        this.dronesList = dronesList;

        connectToDrone();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // setup the drone discoverer and register as listener
        droneDiscoverer.setup();
        droneDiscoverer.addListener(this); // onDronesListUpdated

        // start discovering
        droneDiscoverer.startDiscovering();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean denied = false;
        if (permissions.length == 0) {
            // canceled, finish
            denied = true;
        } else {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    denied = true;
                }
            }
        }

        if (denied) {
            Toast.makeText(this, "At least one permission is missing.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // #############################################################
    // AutonomousFlightControllerListener
    // #############################################################

    @Override
    protected void onStop() {
        super.onStop();
        UnsubscribeIfPresent.dispose(lastKnownLocationDisposable);
    }
}
