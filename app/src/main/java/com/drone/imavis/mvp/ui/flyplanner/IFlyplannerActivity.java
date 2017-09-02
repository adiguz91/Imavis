package com.drone.imavis.mvp.ui.flyplanner;

import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.drone.imavis.mvp.ui.base.IMvpView;

import java.util.List;

/**
 * Created by adigu on 20.08.2017.
 */

public interface IFlyplannerActivity extends IMvpView {

    void onSaveFlyplanSuccess(FlyPlan flyplan);
    void onSaveFlyplanFailed();

    void onStartFlyplanTaskSuccess(FlyPlan flyplan);
    void onStartFlyplanTaskFailed();

    void updateFlyplanNodes(List<GPSCoordinate> waypointGpsCoordinates, List<GPSCoordinate> poiGpsCoordinates);
}