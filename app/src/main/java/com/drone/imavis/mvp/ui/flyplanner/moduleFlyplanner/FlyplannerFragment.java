package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.data.model.GoogleMapExtension;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.base.BaseFragment;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//import com.drone.flyplanner.ui.flyplan.FlyPlanView;


public class FlyplannerFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    @BindView(R.id.flyplannerDraw)
    FlyPlanView flyplannerDrawer;
    @BindView(R.id.flightPlannerNodeActionMenu)
    FlightPlannerNodeActionMenu flightPlannerNodeActionMenu;
    @BindView(R.id.flightPlannerHeaderButtons)
    FlightPlannerHeaderButtons flightPlannerHeaderButtons;
    private GoogleMapFragment googleMapFragment;
    private IFlightPlanner activityListener;
    private Unbinder unbinder;
    private View view;

    private boolean isLoadedCompleted = false;

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

    public FlyPlan getFlyPlan() {
        return activityListener.getFlyplan();
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
        unbinder = ButterKnife.bind(this, view);
        initChildFragments(savedInstanceState);
        flyplannerDrawer.setFlyplanner(this);
        flightPlannerHeaderButtons.initialize();
        flightPlannerNodeActionMenu.initialize();
    }

    /**
     * https://developer.android.com/reference/android/app/Fragment
     *
     * @param savedInstanceState
     */
    private void initChildFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // init GoogleMapFragment
            googleMapFragment = (GoogleMapFragment) getChildFragmentManager().findFragmentById(R.id.flyplannerMap);
            getGoogleMapFragment().setOnMapReadyCallback(this);
            getGoogleMapFragment().setOnMapLoadedCallback(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isLoadedCompleted)
            activityListener.onFlightPlannerLoading();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getGoogleMapFragment().onMapReady(googleMap);
        GoogleMapExtension googleMapExtension = new GoogleMapExtension(getGoogleMapFragment().getMap());
        activityListener.getFlyplan().setMap(googleMapExtension);
        //getGoogleMapFragment().setOnMapLoadedCallback(this);
        activityListener.getFlyplan().getPoints(); // load nodes
        //flyplannerDrawer.setIsLoading(false);
    }

    @Override
    public void onMapLoaded() {
        // Called when the map has finished rendering. This will only be called once.
        //flyplannerDrawer.getController().setFlyPlan(activityListener.getFlyplan());
        flyplannerDrawer.invalidate(); // To force a view to draw
        flyplannerDrawer.setIsLoading(false);
        activityListener.onFlightPlannerLoadingCompleted();
        isLoadedCompleted = true;
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
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
