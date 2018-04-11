package com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.dimension.Size;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Coordinate {

    private float x; // longitude
    private float y; // latitude
    private float z; // elevation

    private GPSCoordinate gpsCoordinate;

    public Coordinate(float x, float y, float z) {
        setCoordinate(x, y, z);
    }

    public Coordinate(double x, double y, double z) {
        setCoordinate((float) x, (float) y, (float) z);
    }

    public Coordinate(float x, float y) {
        setCoordinate(x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setCoordinate(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate toScaleFactor(float scaleFactor) {
        return new Coordinate(
                getX() / scaleFactor,
                getY() / scaleFactor);
    }

    public Coordinate toLeftTop(Size element) {
        int elementLeft = (int) this.getX() - element.getWidth()/2;
        int elementTop = (int) this.getY() - element.getHeight()/2;
        return new Coordinate(elementLeft, elementTop);
    }

    public void setCoordinate(float x, float y) {
        setCoordinate(x, y, 0);
    }

    public GPSCoordinate getGpsCoordinate() {
        return gpsCoordinate;
    }

    public void setGpsCoordinate(GPSCoordinate gpsCoordinate) {
        this.gpsCoordinate = gpsCoordinate;
    }
}