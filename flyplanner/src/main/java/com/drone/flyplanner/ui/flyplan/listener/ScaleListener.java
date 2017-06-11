package com.drone.flyplanner.ui.flyplan.listener;

import android.view.ScaleGestureDetector;

import com.drone.flyplanner.util.constants.classes.CMap;

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