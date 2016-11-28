package com.drone.imavis.flyplan;

import android.util.Size;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Coordinate {

    private float x;
    private float y; // latitude
    private float z;

    public Coordinate(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = 0; // not set
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}