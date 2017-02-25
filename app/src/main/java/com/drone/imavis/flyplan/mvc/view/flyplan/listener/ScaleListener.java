package com.drone.imavis.flyplan.mvc.view.flyplan.listener;

import android.view.ScaleGestureDetector;

import com.drone.imavis.constants.classes.CMap;
import com.drone.imavis.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.flyplan.mvc.view.flyplan.FlyPlanView;

/**
 * Created by adigu on 05.02.2017.
 */

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    private static float scaleFactor = CMap.SCALE_FACTOR_DEFAULT;

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = Math.max(CMap.SCALE_FACTOR_MIN, Math.min(scaleFactor, CMap.SCALE_FACTOR_MAX));
        //Log.w(TAG, "scaling factor: " + mScaleFactor);
        return true;
    }

    public static float getScaleFactor() { return scaleFactor; }
}