package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.GoogleMapExtension;
import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.base.BaseFragment;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

//import com.drone.flyplanner.ui.flyplan.FlyPlanView;


public class FlyplannerFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private GoogleMapFragment googleMapFragment;
    private FlyPlanView flyplannerDrawer;
    private Context context;

    //private boolean showMap;
    private View view;

    private FlyplannerActivity activity;

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
        activity = (FlyplannerActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ButterKnife.bind(getContext(), view);
        flyplannerDrawer = (FlyPlanView) view.findViewById(R.id.flyplannerDraw);

        initChildFragment();
    }

    private void initChildFragment() {
        // load google map fragment
        googleMapFragment = new GoogleMapFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.flyplannerMap, googleMapFragment).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        //googleMapFragment.getMapView().getMapAsync(this); //.setOnMapReadyCallback(this);
        googleMapFragment.setOnMapReadyCallback(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getGoogleMapFragment().onMapReady(googleMap);


        // TODO GoogleMapExtension can be initialized in the GoogleMapFragment
        GoogleMapExtension googleMapExtension = new GoogleMapExtension(getGoogleMapFragment().getMap());
        activity.getFlyplan().setMap(googleMapExtension);

        googleMapFragment.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        FlyPlanController.getInstance().setFlyPlan(activity.getFlyplan());
        flyplannerDrawer.invalidate(); // To force a view to draw
    }

    public GoogleMapFragment getGoogleMapFragment() {
        return googleMapFragment;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

/*
    @Override
    public void onMapTouchReceive(boolean result, MotionEvent event) {
        //boolean result = flyplannerDrawer.onTouchEvent(event);
        if(!result)
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
        else
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }
*/

}
