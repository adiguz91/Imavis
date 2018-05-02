package com.drone.imavis.mvp.ui.searchwlan;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.amulyakhare.textdrawable.TextDrawable;
import com.annimon.stream.Stream;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.local.preference.PreferencesHelper;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.util.CircleMath;
import com.drone.imavis.mvp.util.IWifiUtilCallback;
import com.drone.imavis.mvp.util.WifiUtil;
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
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    WifiUtil wifiUtil;
    private Context context;

    @BindView(R.id.wlanMap)
    RippleBackground wlanMapView;
    @BindView(R.id.centerImage)
    ImageView centerImageButton;
    @BindView(R.id.rootFoundDevices)
    RelativeLayout rootLayout;

    private View positiveAction;
    private EditText passwordInput;

    private boolean isNeverClicked = true;
    private long maxFoundDevices = 6;

    final Handler wlanMapHandler = new Handler();

    // https://wwija.com/computer-internet-technology/46004_scan-results-available-action-return-empty-list-in-android-6-0.html
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000;

    private Point centerPoint;
    private float centerPointRadius;
    private Point rippleCanvasPoint;
    private float rippleRadius;
    private List<Point> devicePositions;

    private boolean busy = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_searchwlan);
        ButterKnife.bind(this);
        context = this;

        // set backbutton color
        final Drawable leftArrow =  getDrawable(R.drawable.abc_ic_ab_back_material);
        leftArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(leftArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set listeners
        wifiUtil.setWifiUtilCallback(wifiUtilCallback);

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }
    }

    private void enableLocation() {
        if (!isEnabledLocation()) {
            String message = "Please enable GPS to find Wifi-Devices.";
            openDialogSettings(Settings.ACTION_LOCATION_SOURCE_SETTINGS, message);
        } else {
            scanWifi();
        }
    }

    private void scanWifi() {
        WifiUtils.withContext(getApplicationContext()).enableWifi(isSuccess -> {
            if (isSuccess) {
                if(rootLayout.getChildCount() > 0)
                    rootLayout.removeAllViews();
                devicePositions = new ArrayList<>();
                devicePositions.add(centerPoint);
                wlanMapView.startRippleAnimation();
                WifiUtils.withContext(getApplicationContext()).scanWifi(scanResults -> {
                    List<ScanResult> filteredScanResults = Stream.of(scanResults).sortBy(scanResult -> Math.abs(scanResult.level)).limit(maxFoundDevices).toList();
                    //List<ScanResult> filteredScanResult = (List) scanResults.stream().sorted(Comparator.comparing(o -> o.level)).limit(maxFoundDevices);
                    Stream.of(filteredScanResults).forEach(scanResult -> createDevice(scanResult, getRandomPointFromRipple()));
                    wlanMapView.stopRippleAnimation();
                    busy = false;
                }).start();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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

        //fabWifiDevice.

        WifiUtils.enableLog(true);
        getWifi();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    //Log.i(TAG, "You have forgotten to specify the parentActivityName in the AndroidManifest!");
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // call this method only if you are on 6.0 and up, otherwise call doGetWifi()
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getWifi() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        } else {
            scanWifiDevices(); // the actual wifi scanning
            if (isNeverClicked)
                centerImageButton.callOnClick();
            isNeverClicked = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                enableLocation();
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
                    if (CircleMath.isCircleCollision(devicePoint, centerPointRadius + 40, randomPoint)) {
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

    private int REQUEST_CODE_SETTINGS_LOCATION = 0;

    /**
     *
     * @param action: example Settings.ACTION_LOCATION_SOURCE_SETTINGS
     */
    private void openDialogSettings(String action, String message) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
            .title("Open Settings") // R.string.title
            .content(message) // R.string.content
            .positiveText("AGREE") // R.string.agree
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog dialog, DialogAction which) {
                    // TODO
                    startActivityForResult(new Intent(action), REQUEST_CODE_SETTINGS_LOCATION);
                }
            })
            .cancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    busy = false;
                }
            })
            .dismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    busy = false;
                }
            })
            .show();
    }

    private boolean isEnabledLocation() {
        //LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return ((LocationManager)context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_SETTINGS_LOCATION) {
            // Make sure the request was successful
            //if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) { }
            if (!isEnabledLocation())
                return;
            scanWifi();
        }
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

    private ImageView addImageViewToRoot(ViewGroup root, ScanResult scanResult, Point point) {
        point = CircleMath.centerPoint(point, (int) centerPointRadius);
        ImageView imageView = new ImageView(getApplicationContext());
        //imageView.setImageDrawable(getDrawable(R.drawable.phone2));
        imageView.setImageDrawable(getTextDrawable(scanResult.SSID));
        imageView.setVisibility(View.INVISIBLE);
        imageView.setOnClickListener(view -> {
            //showDialogWifiConnector(scanResult);
            wifiUtil.showDialogConnect(scanResult);
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
                .withBorder(4)
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


