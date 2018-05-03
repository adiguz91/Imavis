package com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;

/**
 * Created by adigu on 03.02.2017.
 */

public class Square<T> extends GeometricShape {

    private int radius;
    private Class<T> classT;

    public Square(Class<T> classT, Coordinate coordinate, int radius) {
        super(classT, coordinate);
        this.classT = classT;
        this.radius = radius;
    }

    @Override
    public void draw(Canvas canvas) {
        // todo
    }
}
