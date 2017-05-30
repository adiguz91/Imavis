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
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map.GoogleMapFragment;
import com.drone.imavis.mvp.util.ProgressGenerator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;


public class FlyplannerFragment extends BaseFragment {

    GoogleMapFragment googleMapFragment;
    GoogleMap googleMap;
    FlyPlanView flyplannerDrawer;

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
        return inflater.inflate(R.layout.fragment_flyplanner, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ButterKnife.bind(getContext(), view);

        flyplannerDrawer = (FlyPlanView) view.findViewById(R.id.flyplannerDraw);

        // load map fragment
        googleMapFragment = new GoogleMapFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flyplannerMapView, googleMapFragment);
        fragmentTransaction.commit();

        //googleMapFragment.getMap();
        //flyplannerDrawer.setMapFragment(googleMapFragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
}
