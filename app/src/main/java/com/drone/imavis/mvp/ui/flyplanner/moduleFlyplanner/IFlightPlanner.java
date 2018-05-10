package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner;

import com.drone.imavis.mvp.data.model.FlyPlan;

public interface IFlightPlanner {

    FlyPlan getFlyplan();

    void onFlightPlannerLoading();

    void onFlightPlannerLoadingCompleted();

    void onFlightPlannerMapReady();

    void onFlightPlannerIdle(); // onUpdate(); // Touch ACTION_UP

    void onFlightPlannerPressStart();

    void onFlightPlannerPressCancel();

    /*
    void onBeforeTakeOff();
    void onTakeOff();
    void onCancel();
    void onLand(int reasonToLand); // completed
    */
}
