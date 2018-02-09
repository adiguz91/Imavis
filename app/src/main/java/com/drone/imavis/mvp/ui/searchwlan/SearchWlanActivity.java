package com.drone.imavis.mvp.ui.searchwlan;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.services.flyplan.mvc.view.SheetFab;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.util.CircleMath;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.skyfishjy.library.RippleBackground;
import com.thanosfisherman.wifiutils.WifiUtils;

import org.apache.commons.lang3.StringUtils;

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
    @BindView(R.id.centerImage)
    ImageView centerImageButton;
    @BindView(R.id.rootFoundDevices)
    RelativeLayout rootLayout;
    @BindView(R.id.fabWifiDevice)
    SheetFab fabWifiDevice;
    @BindView(R.id.fabWifiSheet)
    View sheetViewWifi;
    @BindView(R.id.overlayWifi)
    View overlayWifi;

    final Handler wlanMapHandler = new Handler();

    // https://wwija.com/computer-internet-technology/46004_scan-results-available-action-return-empty-list-in-android-6-0.html
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000;

    private Point centerPoint;
    private float centerPointRadius;
    private Point rippleCanvasPoint;
    private float rippleRadius;
    private List<Point> devicePositions;

    private boolean busy = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int[] location = new int[2];
        centerImageButton.getLocationInWindow(location);
        //centerPoint = new Point(location[0], location[1]);
        centerPoint = new Point(wlanMapView.getMeasuredWidth() / 2, wlanMapView.getMeasuredHeight() / 2);
        centerPointRadius = centerImageButton.getLayoutParams().width / 2;

        wlanMapView.getLocationInWindow(location);
        Point wlanMapViewPoint = new Point(location[0], location[1]);

        rippleRadius = (int) ((wlanMapView.getMeasuredWidth() / 2) - (centerPointRadius));
        //rippleCanvasPoint = new Point((int)centerPointRadius/2, wlanMapViewPoint.y/2);
        rippleCanvasPoint = new Point(0, 0);

        WifiUtils.enableLog(true);
        getWifi();
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

        initFabSheet();

        //fabWifiDevice.hide();
        rootLayout.setOnTouchListener((view, motionEvent) -> {
            Point touchedPoint = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
            for (Point point : devicePositions) {
                if (CircleMath.isPointInsideCircle(point, touchedPoint, centerPointRadius+30)) {
                    fabWifiDevice.setLeft(point.x);
                    fabWifiDevice.setTop(point.y);
                    fabWifiDevice.callOnClick();
                    break;
                }
            }
            return false;
        });


    }

    private void initFabSheet() {

        int sheetColor = getResources().getColor(R.color.blue_normal);
        int fabColor = getResources().getColor(R.color.accent_color);

        fabWifiDevice.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
        fabWifiDevice.setRippleColor(getResources().getColor(R.color.transparent));
        fabWifiDevice.setElevation(0);
        fabWifiDevice.setCompatElevation(0);

        // Initialize material sheet FAB
        MaterialSheetFab materialSheetFab = new MaterialSheetFab<>(fabWifiDevice, sheetViewWifi, overlayWifi,
                sheetColor, fabColor);
    }

    // call this method only if you are on 6.0 and up, otherwise call doGetWifi()
    private void getWifi() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        } else {
            scanWifiDevices(); // the actual wifi scanning
            centerImageButton.callOnClick();
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

    private void scanWifiDevices() {
        centerImageButton.setOnClickListener(view -> {
            if (busy)
                return;
            busy = true;
            wlanMapHandler.postDelayed(() -> {
                WifiUtils.withContext(getApplicationContext()).enableWifi(isSuccess -> {
                    if (isSuccess) {
                        if(rootLayout.getChildCount() > 0)
                            rootLayout.removeAllViews();
                        devicePositions = new ArrayList<>();
                        devicePositions.add(centerPoint);
                        wlanMapView.startRippleAnimation();
                        WifiUtils.withContext(getApplicationContext()).scanWifi(scanResults -> {
                            for (ScanResult scanResult : scanResults) {
                                createDevice(scanResult, getRandomPointFromRipple());
                            }
                            wlanMapView.stopRippleAnimation();
                            busy = false;
                        }).start();
                    }
                });
            }, 0);
        });
    }

    private Point getRandomPointFromRipple() {
        Point randomPoint = null;
        if (devicePositions.isEmpty()) {
            randomPoint = CircleMath.getRandomPointFromCircle(centerPoint, rippleRadius);
        } else {
            boolean check;
            while (true) {
                check = true;
                randomPoint = CircleMath.getRandomPointFromCircle(centerPoint, rippleRadius);
                for (Point devicePoint : devicePositions) {
                    if (CircleMath.isCircleCollision(devicePoint, centerPointRadius + 30, randomPoint)) {
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

    private void createDevice(ScanResult scanResult, Point point) {
        ImageView imageView = addImageViewToRoot(rootLayout, scanResult, point);
        AnimatorSet animatorSet = createAnimation(imageView);
        imageView.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    private ImageView addImageViewToRoot(ViewGroup root, ScanResult scanResult, Point point) {
        point = CircleMath.centerPoint(point, (int) centerPointRadius);
        ImageView imageView = new ImageView(getApplicationContext());
        //imageView.setImageDrawable(getDrawable(R.drawable.phone2));
        imageView.setImageDrawable(getTextDrawable(scanResult.SSID));
        imageView.setVisibility(View.INVISIBLE);

        String wifiPassword = "";
        imageView.setOnClickListener(view -> {
            // connect to wifi
            WifiUtils.withContext(getApplicationContext())
                .connectWith(scanResult.SSID, wifiPassword)
                .onConnectionResult(isSuccess -> {
                    // success toast message, goBack
                })
                .start();
        });

        int diameter = (int) centerPointRadius * 2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(diameter, diameter);
        params.leftMargin = point.x; //rippleCanvasPoint.x + point.x;
        params.topMargin = point.y; //rippleCanvasPoint.y + point.y;
        root.addView(imageView, params);
        return imageView;
    }

    public TextDrawable getTextDrawable(String text) {
        int maxTextLength = 10;
        if(!text.isEmpty()) {
            text = StringUtils.abbreviate(text, maxTextLength);
        }
        return TextDrawable.builder().beginConfig()
                .useFont(Typeface.DEFAULT)
                .fontSize(toPx(10))
                .textColor(0xfff58559)
                .bold()
                .endConfig().buildRound(text, Color.DKGRAY );
    }

    public int toPx(int dp) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
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


