package com.drone.imavis.mvp.util.listener;

/**
 * Created by adigu on 22.12.2017.
 */

public interface OnEventListener<T, E> {
    public void onSuccess(T data);
    public void onFailure(E errorResult); // exception or error type
}