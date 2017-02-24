package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;

import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;

/**
 * Created by adigu on 03.02.2017.
 */

public class Square<T> extends GeometricShape {

    private Class<T> type;
    public Class<T> getType() { return type; }

    public Square(Class<T> type, Coordinate coordinate) {
        super(type, coordinate);
        this.type = type;
    }

    @Override
    public void draw(Canvas canvas) {

    }

}
