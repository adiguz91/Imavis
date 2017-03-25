package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric;

import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.activities.MainActivity;
import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.IShape;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by adigu on 03.02.2017.
 */

public abstract class GeometricShape<T> implements IShape {

    public GeometricShape(Class<T> classNode, Coordinate coordinate) {
        this.classT = classNode;
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
/*
    public LatLng getCoordinateGPS() {
        return MainActivity.getGPSfromScreen(getCoordinate());
    }
*/
    public int getBorder() {
        return border;
    }
    public void setBorder(int border) {
        this.border = border;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getHigherBackgroundColor() {
        return higherBackgroundColor;
    }
    public void setHigherBackgroundColor(int higherBackgroundColor) {
        this.higherBackgroundColor = higherBackgroundColor;
    }

    public int getBorderColor() {
        return borderColor;
    }
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    private int border;
    private int backgroundColor;
    private int higherBackgroundColor;
    private int borderColor;
    private Coordinate coordinate;
    private Class<T> classT;

    /*
    private T getInstance() {
        try {
            return classT.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }   return null;
    }
    */
}
