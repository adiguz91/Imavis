package com.drone.imavis.flyplan.mvc.model.extensions.coordinates;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Coordinate {

    private float x;
    private float y;
    private float z;

    public Coordinate(float x, float y, float z) {
        setCoordinate(x, y, z);
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

    public void setCoordinate(float x, float y) {
        setCoordinate(x, y, 0);
    }
}