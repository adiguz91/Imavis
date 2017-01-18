package com.drone.imavis.webodm.model;

/**
 * Created by Adrian on 15.06.2016.
 */
public interface VolleyResponseListener {
    void onError(String message);
    void onResponse(Object response);
}
