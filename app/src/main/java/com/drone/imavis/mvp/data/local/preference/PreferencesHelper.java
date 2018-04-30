package com.drone.imavis.mvp.data.local.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.drone.imavis.mvp.di.ApplicationContext;
import com.drone.imavis.mvp.util.constants.classes.CMap;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by adigu on 18.05.2017.
 */

@Singleton
public class PreferencesHelper implements IPreferencesHelper {

    public static final String PREF_FILE_NAME = "ImavisPreferencesFile";
    private final SharedPreferences preferences;

    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_AUTHORIZATION_TOKEN = "PREF_KEY_AUTHORIZATION_TOKEN";
    private static final String PREF_KEY_FLYPLANVIEW_SCALEFACTOR = "PREF_KEY_FLYPLANVIEW_SCALEFACTOR";
    private static final String PREF_KEY_DRONE_WIFI_SSID = "PREF_KEY_DRONE_WIFI_SSID";

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    @Override
    public int getCurrentUserId() {
        return preferences.getInt(PREF_KEY_CURRENT_USER_ID, 0);
    }

    @Override
    public void setCurrentUserId(int userId) {
        preferences.edit().putLong(PREF_KEY_CURRENT_USER_ID, userId).apply();
    }

    @Override
    public String getAuthorizationToken() {
        return preferences.getString(PREF_KEY_AUTHORIZATION_TOKEN, "");
    }

    @Override
    public void setAuthorizationToken(String token) {
        if(token == null) token = "";
        preferences.edit().putString(PREF_KEY_AUTHORIZATION_TOKEN, token).apply();
    }

    @Override
    public float getFlyplanViewScaleFactor() {
        return preferences.getFloat(PREF_KEY_FLYPLANVIEW_SCALEFACTOR, CMap.SCALE_FACTOR_DEFAULT);
    }

    @Override
    public void setFlyplanViewScaleFactor(float scaleFactor) {
        preferences.edit().putFloat(PREF_KEY_FLYPLANVIEW_SCALEFACTOR, scaleFactor).apply();
    }

    @Override
    public String getDroneWifiSsid() {
        return preferences.getString(PREF_KEY_DRONE_WIFI_SSID, "");
    }

    @Override
    public void setDroneWifiSsid(String ssid) {
        if(ssid == null) ssid = "";
        preferences.edit().putString(PREF_KEY_DRONE_WIFI_SSID, ssid).apply();
    }
}