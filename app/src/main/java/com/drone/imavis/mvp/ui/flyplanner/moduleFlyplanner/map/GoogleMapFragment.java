package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterests;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoints;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

//import com.drone.flyplanner.ui.flyplan.FlyPlanView;

/**
 * Created by adigu on 29.05.2017.
 */

// http://things2notedown.blogspot.co.at/2014/07/how-to-display-mapfragment-inside.html
public class GoogleMapFragment extends Fragment implements GoogleMap.OnCameraIdleListener {

    public TouchableWrapper mTouchView;
    private GoogleMap googleMap;
    private OnMapReadyCallback onMapReadyCallback;
    private LatLng location;
    private MarkerOptions markerOptions;
    private Marker marker;
    private boolean showMap;
    private FlyPlanView flyplannerDrawer;

    private OnMapReadyCallback mOnMapReadyCallback;
    private GoogleMap.OnMapLoadedCallback onMapLoadedCallback;
    private OnScreenCoordinateCallback screenCoordinateCallback;
    MapViewExtended mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.googleMapView); // view.findViewById(R.id.googleMapView);
        mapView.onCreate(savedInstanceState);

        //mTouchView = new TouchableWrapper(getActivity());
        //mTouchView.addView(view);

        //mapView.onResume(); // needed to get the map to display immediately

        // add view
        //View layout2 = LayoutInflater.from(this).inflate(R.layout.fragment_, mLinearLayout, false)

        //flyplannerDrawer = (FlyPlanView) getActivity().findViewById(R.id.flyplannerDraw);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ButterKnife.bind(getContext(), view);
    }

    public void setOnMapReadyCallback(OnMapReadyCallback onMapReadyCallback) {
        if (mOnMapReadyCallback == null) {
            mOnMapReadyCallback = onMapReadyCallback;
            mapView.getMapAsync(mOnMapReadyCallback);
        }
    }

    public void setOnMapLoadedCallback(GoogleMap.OnMapLoadedCallback callback) {
        if (onMapLoadedCallback == null) {
            onMapLoadedCallback = callback;
            googleMap.setOnMapLoadedCallback(onMapLoadedCallback);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getContext());
        this.googleMap = googleMap;

        //LocationEnable();
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.googleMap.setTrafficEnabled(true);
        this.googleMap.setIndoorEnabled(true);
        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        this.googleMap.setOnCameraIdleListener(this);

        //CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(getLocation(), 18);
        //googleMap.moveCamera(cameraUpdateFactory);
        updateMarker(getLocation());
        //flyplannerMap.mapTouchView.setFlyplannerMapListener(this);
    }

    public void setMapType(int mapType) {
        if (googleMap != null)
            googleMap.setMapType(mapType);
    }

    public MapViewExtended getMapView() {
        return mapView;
    }

    public void setMapView(MapViewExtended mapView) {
        this.mapView = mapView;
    }

    public LatLng getLocation() {
        if (location == null)
            location = new LatLng(46.61028, 13.85583);
        return location;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(LatLng location) {
        if (marker != null)
            marker.remove();

        //googleMap.setMinZoomPreference(0.5f);
        //googleMap.setMaxZoomPreference(2.0f);
        markerOptions = new MarkerOptions().position(location).title("Current Location");
        marker = googleMap.addMarker(markerOptions);
        marker.setPosition(location);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
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
        if (marker != null)
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

    // centralize?
    public LatLng getGpsfromScreen(Coordinate coordinate) {
        Projection projection = this.googleMap.getProjection();
        // TODO toPoint in COORDINATE
        Point screenPoint = new Point((int) coordinate.getX(), (int) coordinate.getY());
        return projection.fromScreenLocation(screenPoint);
    }

    public void getCoordinatefromGps(GPSCoordinate gpsCoordinate) {
        /*Projection projection = this.googleMap.getProjection();
        Point screenPosition = projection.toScreenLocation(position);
        Coordinate screenPoint = new Coordinate(screenPosition.x, screenPosition.y);
        return screenPoint;
        */

        if (gpsCoordinate == null)
            screenCoordinateCallback.onScreenCoordinate(null);

        LatLng googleCoordinate = new LatLng(gpsCoordinate.getLatitude(), gpsCoordinate.getLongitude());

        Coordinate screenPosition = null;
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    // ! you can query Projection object here
                    Point screenPoint = getMap().getProjection().toScreenLocation(googleCoordinate);
                    Coordinate screenPosition = new Coordinate(screenPoint.x, screenPoint.y);
                    screenCoordinateCallback.onScreenCoordinate(screenPosition);
                }
            });
        }
    }

    public GoogleMap getMap() {
        return googleMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
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

    /*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
*/
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
        if (!result)
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
        else
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    public void setOnScreenCoordinateCallback(OnScreenCoordinateCallback callback) {
        if (screenCoordinateCallback == null)
            this.screenCoordinateCallback = callback;
    }

    @Override
    public void onCameraIdle() {
        // The camera has stopped moving

        List<GPSCoordinate> waypointGpsCoordinates = new ArrayList<>();
        List<GPSCoordinate> poiGpsCoordinates = new ArrayList<>();

        Waypoints waypoints = FlyPlanController.getInstance().getFlyPlan().getPoints().getWaypoints();
        PointOfInterests pois = FlyPlanController.getInstance().getFlyPlan().getPoints().getPointOfInterests();

        Coordinate coordinate;
        for (int i = 0; i < waypoints.size(); i++) {
            coordinate = waypoints.get(i).getShape().getCoordinate();
            LatLng googleGps = getGpsfromScreen(coordinate);
            GPSCoordinate gpsCoordinate = new GPSCoordinate(googleGps.latitude, googleGps.longitude);
            waypointGpsCoordinates.add(gpsCoordinate);
        }

        for (int i = 0; i < pois.size(); i++) {
            coordinate = pois.get(i).getShape().getCoordinate();
            LatLng googleGps = getGpsfromScreen(coordinate);
            GPSCoordinate gpsCoordinate = new GPSCoordinate(googleGps.latitude, googleGps.longitude);
            poiGpsCoordinates.add(gpsCoordinate);
        }

        ((FlyplannerActivity) getActivity()).updateFlyplanNodes(waypointGpsCoordinates, poiGpsCoordinates);
    }
}
