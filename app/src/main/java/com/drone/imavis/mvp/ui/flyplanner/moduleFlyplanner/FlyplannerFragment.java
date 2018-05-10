package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.GoogleMapExtension;
import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.base.BaseFragment;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.drone.flyplanner.ui.flyplan.FlyPlanView;


public class FlyplannerFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    @BindView(R.id.flyplannerDraw)
    FlyPlanView flyplannerDrawer;
    private GoogleMapFragment googleMapFragment;
    private View view;
    @BindView(R.id.flightPlannerNodeActionMenu)
    FlightPlannerNodeActionMenu flightPlannerNodeActionMenu;
    @BindView(R.id.flightPlannerHeaderButtons)
    FlightPlannerHeaderButtons flightPlannerHeaderButtons;
    private IFlightPlanner activityListener;

    public FlyplannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFlightPlanner)
            activityListener = (IFlightPlanner) context;
        else {
            // Throw an error! or setFlightPlannerListener
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flyplanner, container, false);
        activityComponent().inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initChildFragments();
        flyplannerDrawer.setFlyplanner(this);
        flightPlannerHeaderButtons.initialize();
        flightPlannerNodeActionMenu.initialize();
    }

    private void initChildFragments() {
        // load google map fragment
        googleMapFragment = new GoogleMapFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.flyplannerMap, googleMapFragment).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        activityListener.onFlightPlannerLoading();
        googleMapFragment.setOnMapReadyCallback(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getGoogleMapFragment().onMapReady(googleMap);
        GoogleMapExtension googleMapExtension = new GoogleMapExtension(getGoogleMapFragment().getMap());
        activityListener.getFlyplan().setMap(googleMapExtension);

        getGoogleMapFragment().setOnMapLoadedCallback(this);

        activityListener.getFlyplan().getPoints(); // load nodes
        flyplannerDrawer.setIsLoading(false);

        activityListener.onFlightPlannerLoadingCompleted();
    }

    @Override
    public void onMapLoaded() {
        FlyPlanController.getInstance().setFlyPlan(activityListener.getFlyplan());
        flyplannerDrawer.invalidate(); // To force a view to draw
    }

    @OnClick(R.id.fabSheetItemDeleteWaypoint)
    public void onClickFabSheetItemDelete(LinearLayout button) {
        Node node = flyplannerDrawer.getSelectedActionMenuNode();
        activityListener.getFlyplan().getPoints().removeNode(node);
        flyplannerDrawer.invalidate();
        flightPlannerNodeActionMenu.getActionFabSheetMenuWaypoint().hideSheet();
    }

    @OnClick(R.id.fabSheetItemCloseWaypoint)
    public void onClickFabSheetItemClose(LinearLayout button) {
        activityListener.getFlyplan().toggleClosedOrOpen();
        flyplannerDrawer.invalidate();
        flightPlannerNodeActionMenu.getActionFabSheetMenuWaypoint().hideSheet();
    }

    public GoogleMapFragment getGoogleMapFragment() {
        return googleMapFragment;
    }

    public FlightPlannerNodeActionMenu getFlightPlannerNodeActionMenu() {
        return flightPlannerNodeActionMenu;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityListener = null;
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
}
