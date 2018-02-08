package com.drone.imavis.mvp.ui.searchwlan;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.util.CircleMath;
import com.skyfishjy.library.RippleBackground;
import com.thanosfisherman.wifiutils.WifiUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adigu on 08.05.2017.
 */

// https://github.com/ThanosFisherman/WifiUtils
public class SearchWlanActivity extends BaseActivity {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.searchwlan.SearchWlanActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject
    SearchWlanPresenter searchWlanPresenter;
    private Context context;

    @BindView(R.id.wlanMap)
    RippleBackground wlanMapView;
    @BindView(R.id.foundDevice)
    ImageView foundDevice;
    @BindView(R.id.centerImage)
    ImageView centerImageButton;
    @BindView(R.id.rootScanWifi)
    RelativeLayout rootLayout;

    final Handler wlanMapHandler = new Handler();

    // https://wwija.com/computer-internet-technology/46004_scan-results-available-action-return-empty-list-in-android-6-0.html
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000;

    private Point centerPoint;
    private float centerPointRadius;
    private Point rippleCanvasPoint;
    private float rippleRadius;
    private List<Point> devicePositions;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int[] location = new int[2];
        centerImageButton.getLocationInWindow(location);
        //centerPoint = new Point(location[0], location[1]);
        centerPoint = new Point(wlanMapView.getMeasuredWidth()/2, wlanMapView.getMeasuredHeight()/2);
        centerPointRadius = centerImageButton.getLayoutParams().width / 2;

        wlanMapView.getLocationInWindow(location);
        Point wlanMapViewPoint = new Point(location[0], location[1]);

        rippleRadius = (int) ((wlanMapView.getMeasuredWidth()/2) - (centerPointRadius));
        //rippleCanvasPoint = new Point((int)centerPointRadius/2, wlanMapViewPoint.y/2);
        rippleCanvasPoint = new Point(0,0);

        //rootLayout = findViewById(R.id.rootScanWifi);
        //addImageViewToRoot(rootLayout, rippleCanvasPoint);
        //addImageViewToRoot(rootLayout, centerPoint);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_searchwlan);
        ButterKnife.bind(this);
        context = this;

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }

        WifiUtils.enableLog(true);
        searchWlans();
    }

    // call this method only if you are on 6.0 and up, otherwise call doGetWifi()
    private void getWifi() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        } else {
            load(); // the actual wifi scanning
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            getWifi();
        }
    }

    private void load() {
        WifiUtils.withContext(getApplicationContext()).enableWifi(isSuccess -> {
            if (isSuccess) {
                //searchWlans();
                wlanMapView.startRippleAnimation();
                WifiUtils.withContext(getApplicationContext()).scanWifi(scanResults -> {
                    //foundDevices(scanResults);
                }).start();
            }
        });
    }

    private void searchWlans() {
        centerImageButton.setOnClickListener(view -> {
            devicePositions = new ArrayList<>();

            wlanMapView.startRippleAnimation();
            wlanMapHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    devicePositions.add(centerPoint);
                    List<String> simulation = new ArrayList<>();
                    simulation.add("Test1");
                    simulation.add("Test2");
                    simulation.add("Test3");
                    simulation.add("Test4");
                    simulation.add("Test5");
                    simulation.add("Test6");
                    simulation.add("Test7");
                    simulation.add("Test8");
                    foundDevicesSimulation(simulation);
                }
            }, 5000);
        });
        centerImageButton.callOnClick();
    }


    private void foundDevices(List<ScanResult> scanResults) {
        for (ScanResult scanResult : scanResults) {
            positionDevice(scanResult);
        }
    }

    private void foundDevicesSimulation(List<String> scanResults) {
        for (String scanResult : scanResults) {
            Point point = getRandomPointFromRipple();
            createDevice(point);
        }
        wlanMapView.stopRippleAnimation();
    }

    private Point getRandomPointFromRipple() {
        Point randomPoint = null;

        if (devicePositions.isEmpty()) {
            randomPoint = CircleMath.getRandomPointFromCircle(centerPoint, rippleRadius);
        } else {
            boolean isSearching = true;
            boolean check;
            while (isSearching) {
                check = true;
                randomPoint = CircleMath.getRandomPointFromCircle(centerPoint, rippleRadius);
                for (Point devicePoint : devicePositions) {
                    if (CircleMath.isCircleCollision(devicePoint, centerPointRadius+30, randomPoint)) {
                        check &= false;
                        break;
                    } else
                        check &= true;
                }
                if (check)
                    break; // isSearching = false;
            }
        }

        devicePositions.add(randomPoint);
        return randomPoint;
    }

    private void positionDevice(ScanResult scanResult) {

        Point point = getRandomPointFromRipple();
        createDevice(point);

        /* connect to wifi
        WifiUtils.withContext(getApplicationContext())
                .connectWith("MitsarasWiFi", "MitsarasPassword123")
                .onConnectionResult(this::checkResult)
                .start();
        */
    }

    private void createDevice(Point point) {
        ImageView imageView = addImageViewToRoot(rootLayout, point);
        AnimatorSet animatorSet = createAnimation(imageView);
        imageView.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    private ImageView addImageViewToRoot(ViewGroup root, Point point) {
        point = CircleMath.centerPoint(point, (int)centerPointRadius);
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageDrawable(getDrawable(R.drawable.phone2));
        imageView.setVisibility(View.INVISIBLE);

        int diameter = (int)centerPointRadius * 2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(diameter, diameter);
        params.leftMargin = point.x; //rippleCanvasPoint.x + point.x;
        params.topMargin = point.y; //rippleCanvasPoint.y + point.y;
        root.addView(imageView, params);
        return imageView;
    }

    private AnimatorSet createAnimation(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        return animatorSet;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchWlanPresenter.detachView();
    }
}


