package com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates;

/**
 * Created by Adrian on 26.11.2016.
 */

public class GPSCoordinate {

    private float latitude;
    private float longitude;
    private float elevation; // höhendaten

    public GPSCoordinate(float latitude, float longitude, float elevation) {
        setGPSCoordinate(latitude, longitude, elevation);
    }

    public GPSCoordinate(float latitude, float longitude) {
        setGPSCoordinate(latitude, longitude);
    }

    public float getLatitude() {
        return this.latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public float getElevation() {
        return this.elevation;
    }

    public void setGPSCoordinate(float latitude, float longitude, float elevation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    public void setGPSCoordinate(float latitude, float longitude) {
        setGPSCoordinate(latitude, longitude, Float.NaN);
    }
}