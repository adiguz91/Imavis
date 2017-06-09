package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by adigu on 31.05.2017.
 */

public class FlyplannerMap extends SupportMapFragment {

    public View originalView;
    public TouchableMapWrapper mapTouchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        originalView = super.onCreateView(inflater, parent, savedInstanceState);
        mapTouchView = new TouchableMapWrapper(getActivity());
        mapTouchView.addView(originalView);
        return mapTouchView;
    }

    @Override
    public View getView() {
        return originalView;
    }
}
