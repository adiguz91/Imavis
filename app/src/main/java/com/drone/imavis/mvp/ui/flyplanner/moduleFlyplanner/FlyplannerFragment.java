package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

//import com.drone.flyplanner.ui.flyplan.FlyPlanView;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.base.BaseFragment;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map.GoogleMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class FlyplannerFragment extends BaseFragment {

    private GoogleMapFragment googleMapFragment;
    private FlyPlanView flyplannerDrawer;
    private Context context;

    //private boolean showMap;
    private View view;

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

        // load map fragment
        googleMapFragment = new GoogleMapFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flyplannerMap, googleMapFragment);
        fragmentTransaction.commit();

        return view;
    }

    public GoogleMapFragment getGoogleMapFragment() {
        return googleMapFragment;
    }

    public void setGoogleMapFragment(GoogleMapFragment googleMapFragment) {
        this.googleMapFragment = googleMapFragment;
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
