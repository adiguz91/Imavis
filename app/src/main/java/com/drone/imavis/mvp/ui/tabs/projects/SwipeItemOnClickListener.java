package com.drone.imavis.mvp.ui.tabs.projects;

/**
 * Created by adigu on 29.07.2017.
 */

public interface SwipeItemOnClickListener<T> {

    public void onCallback(SwipeActionButtons action, int position, T item);
}
