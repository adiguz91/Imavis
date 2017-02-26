package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;

import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;

/**
 * Created by adigu on 03.02.2017.
 */

public class Square<T> extends GeometricShape {

    public Square(Class<T> classT, Coordinate coordinate) {
        super(classT, coordinate);
        this.classT = classT;
    }

    @Override
    public void draw(Canvas canvas) {
        // todo
    }

    private Class<T> classT;
}
