package com.drone.imavis.mvp.ui.flyplanner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.dronecontrol.BebopDrone;
import com.drone.imavis.mvp.services.dronecontrol.BebopVideoView;
import com.drone.imavis.mvp.services.dronecontrol.DroneDiscoverer;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.FlyplannerFragment;
import com.drone.imavis.mvp.ui.modelviewer.ModelViewerActivity;
import com.drone.imavis.mvp.util.DialogUtil;
import com.drone.imavis.mvp.util.UnsubscribeIfPresent;
import com.drone.imavis.mvp.util.dronecontroll.AutonomousFlightController;
import com.drone.imavis.mvp.util.dronecontroll.DronePermissionRequestHelper;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerCodec;
import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;
import com.parrot.arsdk.arutils.ARUtilsException;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Inject
    FlyplannerPresenter flyplannerPresenter;
    Context context;
    private FlyPlan flyplan;

    private AutonomousFlightController autonomController;

    private BebopDrone bebopDrone;
    private BebopVideoView bebopVideoView;

    @Inject
    DronePermissionRequestHelper dronePermissionRequestHelper;
    @Inject
    DialogUtil dialogUtil;
    List<ARDiscoveryDeviceService> dronesList;
    ARDiscoveryDeviceService drone;

    private FlyplannerFragment flyplannerFragment;

    private ReactiveLocationProvider locationProvider;
    private Observable<Location> lastKnownLocationObservable;
    private Disposable lastKnownLocationDisposable;

    /** List of runtime permission we need. */
    private static final String[] PERMISSIONS_NEEDED = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    /** Code for permission request result handling. */
    private static final int REQUEST_CODE_PERMISSIONS_REQUEST = 1;

    public DroneDiscoverer droneDiscoverer;

    // this block loads the native libraries
    // it is mandatory
    static {
        ARSDK.loadSDKLibs();
    }

    @BindView(R.id.flyplanner_fab_pc_start)
    FABProgressCircle fabProgressCircleStart;

    @BindView(R.id.flyplanner_fab_start)
    FloatingActionButton fabStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        flyplan = (FlyPlan) getIntent().getParcelableExtra("Flyplan");

        setContentView(R.layout.activity_flyplanner);
        ButterKnife.bind(this);

        flyplannerFragment = (FlyplannerFragment) getSupportFragmentManager().findFragmentById(R.id.flyplanner);
        context = this;

        /* Drone */
        droneDiscoverer = new DroneDiscoverer(this);
        dronePermissionRequestHelper.requestPermission(PERMISSIONS_NEEDED, REQUEST_CODE_PERMISSIONS_REQUEST);

        flyplannerPresenter.attachView(this);


        //getActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //fabProgressCircleStart = (FABProgressCircle) findViewById(R.id.flyplanner_fab_pc_start);
        fabProgressCircleStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabProgressCircleStart.show();
                // todo start the drone to fly
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        fabProgressCircleStart.beginFinalAnimation();
                    }
                }, 5 * 1000);
            }
        });

        //FloatingActionButton fabStart = (FloatingActionButton) findViewById(R.id.flyplanner_fab_start);
        fabStart.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_play)
                .colorRes(R.color.icons)
                .actionBarSize());

        fabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String appDirectory = getApplicationInfo().dataDir;
                String imageDirectory = "/storage/emulated/0/Android/data/com.drone.imavis/images/castle/";
                File imageFileDirectory = new File(imageDirectory);
                Uri imageUriDirectory = Uri.fromFile(imageFileDirectory);
                flyplan.setImageFolderUrl(imageUriDirectory);
                flyplannerPresenter.startFlyplanTask(flyplan);
            }
        });

        fabProgressCircleStart.attachListener(new FABProgressListener() {
            @Override
            public void onFABProgressAnimationEnd() {
                Toast.makeText(FlyplannerActivity.this, "Finished Drone", Toast.LENGTH_LONG).show();
            }
        });

        initLocation();

        // todo: init flyplanner fragment instance

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }
    }

    public FlyPlan getFlyplan() {
        return flyplan;
    }

    // TODO : https://github.com/mcharmas/Android-ReactiveLocation/blob/master/sample/src/main/java/pl/charmas/android/reactivelocation2/sample/BaseActivity.java
    //protected abstract void onLocationPermissionGranted();

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
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            onLocationPermissionGranted();
                        } else {
                            //Toast.makeText(BaseActivity.this, "Sorry, no demo without permission...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //-------------------

    public static String combine (String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flyplanner, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.menu_flyplanner_action_maptype).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_map)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        menu.findItem(R.id.menu_flyplanner_action_findgps).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_crosshairs)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        menu.findItem(R.id.menu_flyplanner_action_lock).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_unlock_alt)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        menu.findItem(R.id.drones_spinner).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_wifi)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {

            case R.id.menu_flyplanner_action_maptype:
                goToActivity(this, ModelViewerActivity.class, new Bundle());
                return true;
            case R.id.menu_flyplanner_action_findgps:
                // TODO
                return true;
            case R.id.menu_flyplanner_action_lock:
                FlyPlanView flyplannerDrawer = (FlyPlanView) ((Activity) context).findViewById(R.id.flyplannerDraw);
                if (item.getTitle().equals("Lock")) {
                    // unlock
                    if (flyplannerDrawer != null)
                        flyplannerDrawer.setIsLocked(false);
                    item.setTitle("Unlock");
                    item.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_unlock_alt)
                            .colorRes(R.color.icons)
                            .actionBarSize());
                } else {
                    // lock
                    if (flyplannerDrawer != null)
                        flyplannerDrawer.setIsLocked(true);
                    item.setTitle("Lock");
                    item.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_lock)
                            .colorRes(R.color.icons)
                            .actionBarSize());
                }
                return true;
            case R.id.drones_spinner:
                droneDiscoverer.startDiscovering();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void updateFlyplanNodes(List<GPSCoordinate> waypointGpsCoordinates, List<GPSCoordinate>  poiGpsCoordinates) {
        // TODO set GPS data to flyplan and save to db!
        Toast.makeText(this, "The camera has stopped moving.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        flyplannerPresenter.saveFlyplan(flyplan);

        // clean the drone discoverer object
        droneDiscoverer.stopDiscovering();
        //mDroneDiscoverer.cleanup();
        //mDroneDiscoverer.removeListener(mDiscovererListener);
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

    private final BebopDrone.Listener mBebopListener = new BebopDrone.Listener() {
        @Override
        public void onDroneConnectionChanged(ARCONTROLLER_DEVICE_STATE_ENUM state) {
            switch (state)
            {
                case ARCONTROLLER_DEVICE_STATE_RUNNING:
                    //mConnectionProgressDialog.dismiss();
                    break;

                case ARCONTROLLER_DEVICE_STATE_STOPPED:
                    // if the deviceController is stopped, go back to the previous activity
                    //mConnectionProgressDialog.dismiss();
                    finish();
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onBatteryChargeChanged(int batteryPercentage) {
            //mBatteryLabel.setText(String.format("%d%%", batteryPercentage));
        }

        @Override
        public void onPilotingStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state) {
            switch (state) {
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                    //mTakeOffLandBt.setText("Take off");
                    //mTakeOffLandBt.setEnabled(true);
                    //mDownloadBt.setEnabled(true);
                    break;
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                    //mTakeOffLandBt.setText("Land");
                    //mTakeOffLandBt.setEnabled(true);
                    //mDownloadBt.setEnabled(false);
                    break;
                default:
                    //mTakeOffLandBt.setEnabled(false);
                    //mDownloadBt.setEnabled(false);
            }
        }

        @Override
        public void onPictureTaken(ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error) {
            //Log.i(TAG, "Picture has been taken");
        }

        @Override
        public void configureDecoder(ARControllerCodec codec) {
            bebopVideoView.configureDecoder(codec);
        }

        @Override
        public void onFrameReceived(ARFrame frame) {
            bebopVideoView.displayFrame(frame);
        }

        @Override
        public void onMatchingMediasFound(int nbMedias) {
            /*
            mDownloadProgressDialog.dismiss();
            mNbMaxDownload = nbMedias;
            mCurrentDownloadIndex = 1;

            if (nbMedias > 0) {
                mDownloadProgressDialog = new ProgressDialog(BebopActivity.this, R.style.AppCompatAlertDialogStyle);
                mDownloadProgressDialog.setIndeterminate(false);
                mDownloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mDownloadProgressDialog.setMessage("Downloading medias");
                mDownloadProgressDialog.setMax(mNbMaxDownload * 100);
                mDownloadProgressDialog.setSecondaryProgress(mCurrentDownloadIndex * 100);
                mDownloadProgressDialog.setProgress(0);
                mDownloadProgressDialog.setCancelable(false);
                mDownloadProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBebopDrone.cancelGetLastFlightMedias();
                    }
                });
                mDownloadProgressDialog.show();
            }
            */
        }

        @Override
        public void onDownloadProgressed(String mediaName, int progress) {
            //mDownloadProgressDialog.setProgress(((mCurrentDownloadIndex - 1) * 100) + progress);
        }

        @Override
        public void onDownloadComplete(String mediaName) {
            /*
            mCurrentDownloadIndex++;
            mDownloadProgressDialog.setSecondaryProgress(mCurrentDownloadIndex * 100);

            if (mCurrentDownloadIndex > mNbMaxDownload) {
                mDownloadProgressDialog.dismiss();
                mDownloadProgressDialog = null;
            }
            */
        }
    };

    /* Drone Discovery Code */

    @Override
    public void onDronesListUpdated(List<ARDiscoveryDeviceService> dronesList) {

        if(dronesList == null || dronesList.isEmpty()) {
            Toast.makeText(this, "No Drones found", Toast.LENGTH_SHORT).show();
            return;
        }


        this.dronesList = dronesList;

        List<String> drones = new ArrayList<>();
        for (ARDiscoveryDeviceService drone : dronesList) {
            drones.add(drone.getName());
        }

        dialogUtil.showDialog(FlyplannerActivity.this, "Found Drones", drones, new String[] { "OK", "Abbrechen" },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {

                        if (button == Dialog.BUTTON_POSITIVE) {

                            ListView lw = ((AlertDialog)dialog).getListView();
                            String serviceName = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());

                            for (ARDiscoveryDeviceService droneService : dronesList)
                            {
                                if(droneService.getName().equals(serviceName)) {
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
                                autonomController.getConnectionState();
                                autonomController.connect();
                                //autonomController.takePicture();

                                flyplannerFragment.getGoogleMapFragment();

                                lastKnownLocationObservable.subscribe(x -> {
                                    GPSCoordinate homeLocation = new GPSCoordinate(x.getLatitude(), x.getLongitude(), x.getAltitude());
                                    autonomController.setHomeLocation(homeLocation);
                                });

                                String localFilepath = autonomController.generateMavlinkFile(flyplan.getPoints(), (short)3); // alt 516
                                autonomController.uploadAutonomousFlightPlan(flyplan, localFilepath);
                                autonomController.startAutonomousFlight(); // "flightPlan.mavlink"

                                bebopDrone = new BebopDrone(context, drone);
                                bebopDrone.addListener(mBebopListener);
                            }
                        }

                    }
                });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // setup the drone discoverer and register as listener
        droneDiscoverer.setup();
        droneDiscoverer.addListener(this); // onDronesListUpdated

        // start discovering
        //droneDiscoverer.startDiscovering();
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

    @Override
    protected void onStop() {
        super.onStop();
        UnsubscribeIfPresent.dispose(lastKnownLocationDisposable);
    }
}
