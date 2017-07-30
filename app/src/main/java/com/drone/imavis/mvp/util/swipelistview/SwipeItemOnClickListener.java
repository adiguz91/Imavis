package com.drone.imavis.mvp.util.swipelistview;

import android.view.View;

import com.drone.imavis.mvp.util.swipelistview.SwipeActionButtons;

/**
 * Created by adigu on 29.07.2017.
 */

public interface SwipeItemOnClickListener<T> {

    public void onCallback(View view, SwipeActionButtons action, int position, T item);
}
