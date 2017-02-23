package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.simple;

import android.graphics.Canvas;

import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;

/**
 * Created by adigu on 03.02.2017.
 */

public class Line extends SimpleIShape {

    public Line() {

    }

    private Coordinate startCoordinate;
    private Coordinate endCoordinate;
    private int lineColor;
    private int strokeWidth;

    @Override
    public void draw(Canvas canvas) {

    }
}
