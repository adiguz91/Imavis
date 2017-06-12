package com.drone.flyplanner.data.model.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;

import com.drone.flyplanner.util.models.coordinates.Coordinate;

/**
 * Created by adigu on 03.02.2017.
 */

public class Square<T> extends GeometricShape {

    private int radius;

    public Square(Class<T> classT, Coordinate coordinate, int radius) {
        super(classT, coordinate);
        this.classT = classT;
        this.radius = radius;
    }

    @Override
    public void draw(Canvas canvas) {
        // todo
    }

    @Override
    public void draw(Canvas canvas, float scaleFactor) {
        // TODO
    }


    private Class<T> classT;
}
