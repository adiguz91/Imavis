package com.drone.imavis.mvp.util.listener;

/**
 * Created by adigu on 22.12.2017.
 */

public interface OnEventListener<T, E> {
    void onSuccess(T data);

    void onFailure(E errorResult); // exception or error type
}