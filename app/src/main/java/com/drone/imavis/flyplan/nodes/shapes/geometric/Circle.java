package com.drone.imavis.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.flyplan.coordinates.Coordinate;
import com.drone.imavis.flyplan.dimension.Size;

/**
 * Created by adigu on 03.02.2017.
 */

public class Circle extends GeometricShape {

    public Circle(Coordinate coordinate) {
        super(coordinate);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, float scalingFactor) {
        Coordinate centralizedCoordinate = centralizedCoordinate(getCoordinate(), getSize());
        Coordinate cartesianCoordinate = getScaledCoordinate(centralizedCoordinate, scalingFactor);

        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), getSize() + getBorder(), paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), getSize(), paint);

        //paint.setColor(Color.RED);
    }

    public Coordinate getScaledCoordinate(Coordinate originalPoint, float scalingFactor) {
        float newX, newY;
        newX = originalPoint.getX() / scalingFactor;
        newY = originalPoint.getY() / scalingFactor;
        return new Coordinate(newX, newY);
    }

}
