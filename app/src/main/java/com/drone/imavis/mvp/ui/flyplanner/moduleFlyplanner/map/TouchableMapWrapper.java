package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;


/**
 * Created by adigu on 31.05.2017.
 */

public class TouchableMapWrapper extends FrameLayout {

    public TouchableMapWrapper(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent (MotionEvent event) {

        boolean result = super.onTouchEvent(event);

        if (flyplannerListener != null)
            flyplannerListener.onMapTouchReceive(result, event);
        return !result;
    }

    public void setFlyplannerMapListener(OnMapTouchListener flyplannerListener) {
        this.flyplannerListener = flyplannerListener;
    }

    public interface OnMapTouchListener {
        public void onMapTouchReceive(boolean result, MotionEvent event);
    }

    private OnMapTouchListener flyplannerListener;
}