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
    public boolean onTouchEvent(MotionEvent event) {

        //boolean test = super.onTouchEvent(event);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                //MainActivity.mMapIsTouched = true;
                break;

            case MotionEvent.ACTION_UP:
                //MainActivity.mMapIsTouched = false;
                break;
        }
        return super.onTouchEvent(event);
    }
}