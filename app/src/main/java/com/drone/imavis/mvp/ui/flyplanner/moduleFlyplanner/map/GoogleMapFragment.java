package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.FlyplannerFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by adigu on 29.05.2017.
 */

// http://things2notedown.blogspot.co.at/2014/07/how-to-display-mapfragment-inside.html
public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    private GoogleMap googleMap;
    private OnMapReadyCallback onMapReadyCallback;
    private FlyPlanView flyplannerDrawer;

    private LatLng location;
    private MarkerOptions markerOptions;
    private Marker marker;
    private boolean showMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flyplanner, container, false);

        //flyplannerDrawer = (FlyPlanView) view.findViewById(R.id.flyplanner);

        // Gets the MapView from the XML layout and creates it
        //mapView = (MapView) view.findViewById(R.id.flyplannerMapView);
        //mapView.onCreate(savedInstanceState);
        //mapView.getMapAsync(this);



        //mapView.setOnTouchListener(new View.OnTouchListener() {
        //    @Override
        //    public boolean onTouch(View v, MotionEvent event) {
        //        return false;
        //    }
        //});

        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ButterKnife.bind(getContext(), view);


    }

    public LatLng getLocation() {
        if(location == null)
            location = new LatLng(46.61028, 13.85583);
        return location;
    }
    public Marker getMarker() {
        return marker;
    }

    private void LocationEnable() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public void Zoom(float zoomFactor) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLocation(), zoomFactor * 10 + 2));
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(googleMap.getMaxZoomLevel() - 0.5f));
    }

    public void updateMarker(LatLng location) {
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
    public void onMapReady(GoogleMap googleMap) {
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getContext());

        //LocationEnable();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        //CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(getLocation(), 18);
        //googleMap.moveCamera(cameraUpdateFactory);
        this.googleMap = googleMap;
        updateMarker(getLocation());

        //flyplannerDrawer.setFlyplannerListener(this);
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

    public GoogleMap getMap() {
        return googleMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //@Override
    public void onMapClick(LatLng latLng) {
        /*boolean isHandled = false; //handled by flyplannerDrawer
        if(!isHandled) {
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
        }
        else {
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
        }*/
    }

    //@Override
    public void onCompleteHandling(boolean result) {
        if(!result)
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
        else
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}
