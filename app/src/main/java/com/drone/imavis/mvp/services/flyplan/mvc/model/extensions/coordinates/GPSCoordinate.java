package com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Adrian on 26.11.2016.
 */

@Entity
public class GPSCoordinate {

    @Id(autoincrement = true)
    private Long id;

    private double latitude; // y
    private double longitude; // x
    private double altitude; // höhendaten

    public GPSCoordinate(double latitude, double longitude, double altitude) {
        setGPSCoordinate(latitude, longitude, altitude);
    }

    public GPSCoordinate(double latitude, double longitude) {
        setGPSCoordinate(latitude, longitude);
    }

    @Generated(hash = 1972717532)
    public GPSCoordinate(Long id, double latitude, double longitude, double altitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    @Generated(hash = 1569993542)
    public GPSCoordinate() {
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setGPSCoordinate(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public void setGPSCoordinate(double latitude, double longitude) {
        setGPSCoordinate(latitude, longitude, Double.NaN);
    }

    public Coordinate convertToCoordinate() {
        return new Coordinate(longitude, latitude, altitude);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}