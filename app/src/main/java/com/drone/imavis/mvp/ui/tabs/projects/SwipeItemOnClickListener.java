package com.drone.imavis.mvp.ui.tabs.projects;

import android.view.View;

/**
 * Created by adigu on 29.07.2017.
 */

public interface SwipeItemOnClickListener<T> {

    public void onCallback(View view, SwipeActionButtons action, int position, T item);
}
