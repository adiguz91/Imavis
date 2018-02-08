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
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.util.CicleMath;
import com.skyfishjy.library.RippleBackground;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiScan.ScanResultsListener;
import com.thanosfisherman.wifiutils.wifiState.WifiStateListener;

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

    @Inject SearchWlanPresenter searchWlanPresenter;
    private Context context;

    @BindView(R.id.wlanMap) RippleBackground wlanMapView;
    @BindView(R.id.foundDevice) ImageView foundDevice;
    @BindView(R.id.centerImage) ImageView centerImageButton;
    final Handler wlanMapHandler = new Handler();

    // https://wwija.com/computer-internet-technology/46004_scan-results-available-action-return-empty-list-in-android-6-0.html
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000;
    private Point centerPoint;
    private float centerPointRadius;
    private List<Point> devicePositions;

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        int[] location = new int[2];
        centerImageButton.getLocationInWindow(location);
        centerPoint = new Point(location[0], location[1]);
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

        centerPointRadius = centerImageButton.getLayoutParams().width;
        centerPoint = new Point(centerImageButton.getLeft(), centerImageButton.getTop());
        devicePositions = new ArrayList<>();

        //load();
        //getWifi();
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
        WifiUtils.withContext(getApplicationContext()).enableWifi(new WifiStateListener() {
            @Override
            public void isSuccess(boolean isSuccess) {
                //searchWlans();
                wlanMapView.startRippleAnimation();
                WifiUtils.withContext(getApplicationContext()).scanWifi(new ScanResultsListener() {
                    @Override
                    public void onScanResults(@NonNull List<ScanResult> scanResults) {

                        //foundDevices(scanResults);

                    }
                }).start();
            }
        });
    }





    private void searchWlans() {
        centerImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wlanMapView.startRippleAnimation();
                wlanMapHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> simulation = new ArrayList<>();
                        simulation.add("Test1");
                        simulation.add("Test2");
                        //simulation.add("Test3");
                        //simulation.add("Test4");
                        //simulation.add("Test5");
                        foundDevicesSimulation(simulation);
                    }
                },5000);
            }
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
            positionDeviceSimulation(scanResult);
        }
    }



    private Point getRandomPointFromRipple() {
        Point point = null;

        int maxRippleRadius = (int) (centerPoint.x - centerPointRadius);

        if (devicePositions.isEmpty())
            point = CicleMath.getRandomPointFromCircle(centerPoint, maxRippleRadius);
        else {
            boolean isSearching = true;
            boolean check = true;
            while (isSearching) {
                point = CicleMath.getRandomPointFromCircle(centerPoint, maxRippleRadius);
                check = true;
                for (Point devicePoint : devicePositions) {
                    if(CicleMath.isInsideCircle(devicePoint, centerPointRadius, point)) {
                        check &= false;
                        break;
                    }
                    else
                        check &= true;
                }
                if(check)
                    break;
            }
        }

        devicePositions.add(point);
        return point;
    }

    private void positionDeviceSimulation(String scanResult) {
        Point point = getRandomPointFromRipple();
        createDevice(point);
    }

    private void positionDevice(ScanResult scanResult){

        Point point = getRandomPointFromRipple();
        createDevice(point);

        /* connect to wifi
        WifiUtils.withContext(getApplicationContext())
                .connectWith("MitsarasWiFi", "MitsarasPassword123")
                .onConnectionResult(this::checkResult)
                .start();
        */
    }


    private ImageView createImageView() {
        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageDrawable(getDrawable(R.drawable.phone2));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                RelativeLayout.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(lp);
        return iv;
    }

    private void createDevice(Point point) {

        ImageView imageView = createImageView();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);

        RelativeLayout root = (RelativeLayout) findViewById(R.id.rootScanWifi);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(168, 168);

        int rippleRadius = (int) (centerPoint.x - centerPointRadius);
        Point canvasPoint = new Point((int) centerPointRadius, centerPoint.y - rippleRadius);
        params.leftMargin = canvasPoint.x + point.x;
        params.topMargin = canvasPoint.y + point.y;
        root.addView(imageView, params);

        imageView.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchWlanPresenter.detachView();
    }
}


