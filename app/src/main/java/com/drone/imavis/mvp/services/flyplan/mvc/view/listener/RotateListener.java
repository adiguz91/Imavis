package com.drone.imavis.mvp.services.flyplan.mvc.view.listener;

import com.almeros.android.multitouch.RotateGestureDetector;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;

public class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {

    private float rotationDegrees = 0.f;
    private FlyPlanView parent; // flightPlanDrawer

    public RotateListener(FlyPlanView parent) {
        this.parent = parent;
    }

    @Override
    public boolean onRotate(RotateGestureDetector detector) {
        rotationDegrees -= detector.getRotationDegreesDelta();
        parent.setRotationDegrees(rotationDegrees);
        return true;
    }

}
