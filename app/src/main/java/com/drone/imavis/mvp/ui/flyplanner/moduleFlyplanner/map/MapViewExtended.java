package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.MapView;

public class MapViewExtended extends MapView {
    public View mapView;
    public TouchableWrapper touchView;
    private MapViewExtended.OnTouchListener listener;

    public MapViewExtended(Context context) {
        super(context);
    }

    public MapViewExtended(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setOnTouchListener(MapViewExtended.OnTouchListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("MapViewExtended", "TOUCHED");
        /*switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                MainActivity.mMapIsTouched = true;
                break;

            case MotionEvent.ACTION_UP:
                MainActivity.mMapIsTouched = false;
                break;
        }*/
        return super.dispatchTouchEvent(event);
    }

    public interface OnTouchListener {
        void onTouch();
    }

}