package com.drone.imavis.mvp.services.flyplan.mvc.view.listener;

import android.view.ScaleGestureDetector;

import com.drone.imavis.mvp.data.local.preference.PreferencesHelper;
import com.drone.imavis.mvp.util.constants.classes.CMap;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by adigu on 05.02.2017.
 */

@Singleton
public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    private PreferencesHelper preferencesHelper;
    private float scaleFactor = CMap.SCALE_FACTOR_DEFAULT;

    @Inject
    public ScaleListener(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = Math.max(CMap.SCALE_FACTOR_MIN, Math.min(scaleFactor, CMap.SCALE_FACTOR_MAX));

        // save to preferences
        preferencesHelper.setFlyplanViewScaleFactor(scaleFactor);
        return true;
    }

    /*@Override
    public boolean onScale(ScaleGestureDetector detector) {
        double zoom = googleMap.getCameraPosition().zoom;
        zoom = zoom + Math.log(detector.getScaleFactor()) / Math.log(1.5d);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, (float) zoom);
        googleMap.moveCamera(update);
        return true;
    }*/
}