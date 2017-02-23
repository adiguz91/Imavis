package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric;

import android.graphics.Color;

import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.IShape;

/**
 * Created by adigu on 03.02.2017.
 */

public abstract class GeometricShape implements IShape {

    public GeometricShape(Coordinate coordinate) {
        setCoordinate(coordinate);
    }

    public Coordinate centralizedCoordinate(Coordinate coordinate, int size) {
        float centeredX = coordinate.getX() - size/2;
        float centeredY = coordinate.getY() - size/2;

        if(centeredX < 0)
            centeredX = 0;

        if(centeredY < 0)
            centeredY = 0;

        //coordinate.setCoordinate(centeredX, centeredY);
        return coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    private int size = 50;
    private int border = 10;
    private int backgroundColor = Color.WHITE;
    private int borderColor = Color.GREEN;
    private Coordinate coordinate;
}
