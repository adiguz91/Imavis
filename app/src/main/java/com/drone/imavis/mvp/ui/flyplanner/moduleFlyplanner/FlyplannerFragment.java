package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.base.BaseFragment;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map.FlyplannerMap;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map.GoogleMapFragment;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map.TouchableMapWrapper;
import com.drone.imavis.mvp.util.ProgressGenerator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class FlyplannerFragment extends BaseFragment implements OnMapReadyCallback, TouchableMapWrapper.OnMapTouchListener {

    //MapView mapView;
    FlyplannerMap flyplannerMap;
    private GoogleMap googleMap;
    FlyPlanView flyplannerDrawer;
    Context context;

    private LatLng location;
    private MarkerOptions markerOptions;
    private Marker marker;
    private boolean showMap;

    private View view;

    private TouchableMapWrapper touchableMapWrapper;

    public FlyplannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flyplanner, container, false);

        flyplannerDrawer = (FlyPlanView) view.findViewById(R.id.flyplannerDraw);

        flyplannerMap = (FlyplannerMap) getChildFragmentManager().findFragmentById(R.id.flyplannerMapFragment);

        //flyplannerMap.onCreate(savedInstanceState);
        flyplannerMap.getMapAsync(this);

        return view;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ButterKnife.bind(getContext(), view);



        // load map fragment
        //googleMapFragment = new GoogleMapFragment();
        //FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        //fragmentTransaction.replace(R.id.flyplannerMapView, googleMapFragment);
        //fragmentTransaction.commit();



        //googleMapFragment.getMap();
        //flyplannerDrawer.setMapFragment(googleMapFragment);
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

        flyplannerMap.mapTouchView.setFlyplannerMapListener(this);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        //
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        flyplannerMap.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flyplannerMap.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        flyplannerMap.onPause();
    }

    public LatLng getLocation() {
        if(location == null)
            location = new LatLng(46.61028, 13.85583);
        return location;
    }
    public Marker getMarker() {
        return marker;
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
    public void onMapTouchReceive(boolean result, MotionEvent event) {
        //boolean result = flyplannerDrawer.onTouchEvent(event);
        if(!result)
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
        else
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}
