package com.drone.imavis.mvp.ui.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.view.ActionButtons;
import com.drone.imavis.mvp.services.flyplan.mvc.view.listener.GPSTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainFlyplanner extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {

    private static final String TAG = "MainPlyplanner";
    private static RelativeLayout layout;
    private static ActionButtons actionButtons;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private LatLng location;
    private MarkerOptions markerOptions;
    private Marker marker;
    private boolean showMap;
    private View flyplan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_flyplanner);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupWindowAnimations();
        layout = (RelativeLayout) findViewById(R.id.content_main_flyplanner);
        actionButtons = new ActionButtons(layout);

        showMap = initMap(); //checkPlayServices() && initMap();

        if(showMap) {
            runtimePermissions();

            Intent i = new Intent(getApplicationContext(), GPSTracker.class);
            startService(i);
        }

        flyplan = findViewById(R.id.flypan);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) { //dispatchTouchEvent

        boolean isHandled = false;
        isHandled = super.onTouchEvent(event);
        //boolean isHandled = flyplan.onTouchEvent(event);
        //isHandled = FlyPlanView.getIsHandledTouch();
        if(!isHandled) {
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
        }
        else {
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
        }
        return !isHandled;
        //return true;
    }

    private boolean runtimePermissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                //enable_buttons();
            } else {
                runtimePermissions();
            }
        }
    }

    private void setupWindowAnimations() {
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.slide);
        getWindow().setExitTransition(slide);
    }

    private boolean initMap() {
        if(googleMap == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapFragment);
            if(mapFragment != null) {
                //mapFragment.onCreate(null);
                //mapFragment.onResume();
                mapFragment.getMapAsync(this);
            }
        }
        return (googleMap != null);
    }

    public LatLng getLocation() {
        if(location == null)
            location = new LatLng(46.61028, 13.85583);
        return location;
    }

    public Marker getMarker() {
        return marker;
    }

    private void LoadFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_my_location_black_34dp);

        fab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#5386E4")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMarker(getLocation());
                // stopService(i);
            }
        });
    }

    private static List<Button> buttons = new ArrayList<Button>() {};
    public static void addActionButtons(Coordinate coordinate) {
        buttons = actionButtons.getActionButtons(coordinate);
        for (Button button : buttons) {
            layout.removeView(button);
            layout.addView(button);
        }
    }

    public static void removeActionButtons() {
        for (Button button : buttons) {
            layout.removeView(button);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);

        //LocationEnable();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap = googleMap;
        //CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(getLocation(), 18);
        //googleMap.moveCamera(cameraUpdateFactory);

        updateMarker(getLocation());

    }

    private void LocationEnable() {
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
        googleMap.setMyLocationEnabled(true);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String position = (String) intent.getExtras().get("coordinates");
                    String[] positionArray = position.split(" ");
                    location = new LatLng(Double.parseDouble(positionArray[0]), Double.parseDouble(positionArray[1]));
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    /*
    // centralize?
    public static LatLng getGPSfromScreen(Coordinate coordinate) {
        Projection projection = googleMap.getProjection();
        Point screenPoint = new Point((int)coordinate.getX(), (int)coordinate.getY());
        return projection.fromScreenLocation(screenPoint);
    }

    public static Coordinate getCoordinatefromGPS(LatLng position) {
        Projection projection = googleMap.getProjection();
        Point screenPosition = projection.toScreenLocation(position);
        Coordinate screenPoint = new Coordinate(screenPosition.x, screenPosition.y);
        return screenPoint;
    }
    */

    public void Zoom(float zoomFactor) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLocation(), zoomFactor * 10 + 2));
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(googleMap.getMaxZoomLevel() - 0.5f));
    }

    private void updateMarker(LatLng location) {
        if(marker != null)
            marker.remove();

        //googleMap.setMinZoomPreference(0.5f);
        //googleMap.setMaxZoomPreference(2.0f);
        markerOptions = new MarkerOptions().position(getLocation()).title("Marker in Villach");
        marker = googleMap.addMarker(markerOptions);
        marker.setPosition(getLocation());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(getLocation()));
        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        int i = 0;

    }
}
