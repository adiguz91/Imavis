package com.drone.imavis.flyplan.nodes.shapes.simple;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.drone.imavis.flyplan.coordinates.Coordinate;
import com.drone.imavis.flyplan.dimension.Size;

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
    public void draw(Canvas canvas, Paint paint, float scalingFactor) {

    }
}
