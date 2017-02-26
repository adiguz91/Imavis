package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric;

import android.graphics.Color;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.data.poi.PointOfInterest;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.IShape;

/**
 * Created by adigu on 03.02.2017.
 */

public abstract class GeometricShape<T> implements IShape {

    public GeometricShape(Class<T> type, Coordinate coordinate) {
        this.type = type;
        this.coordinate = coordinate;
        initByType();
    }

    private void initByType() {
        if(type == Waypoint.class)
            initWaypoint();
        if(type == PointOfInterest.class)
            initPOI();
    }

    private void initWaypoint() {
        setBackgroundColor(Color.parseColor(CColor.WAYPOINT_CIRCLE));
        setBorderColor(Color.parseColor(CColor.WAYPOINT_CIRCLE_BORDER));
        setBorder(CShape.WAYPOINT_CIRCLE_BORDERSIZE);
    }

    private void initPOI() {
        setBackgroundColor(Color.parseColor(CColor.POI_CIRCLES.get(0)));
        setBorderColor(Color.parseColor(CColor.POI_CIRCLE_BORDER));
        setBorder(CShape.POI_CIRCLE_BORDERSIZE);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

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

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    private int border = 10;
    private int backgroundColor = Color.WHITE;
    private int borderColor = Color.GREEN;
    private Coordinate coordinate;
    private Class<T> type;
}
