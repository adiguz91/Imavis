package com.drone.imavis.mvp.data.local.preference;

/**
 * Created by adigu on 18.05.2017.
 */

public interface IPreferencesHelper {

    int getCurrentUserId();
    void setCurrentUserId(int userId);

    String getAuthorizationToken();
    void setAuthorizationToken(String token);

    float getFlyplanViewScaleFactor();
    void setFlyplanViewScaleFactor(float scaleFactor);
}
