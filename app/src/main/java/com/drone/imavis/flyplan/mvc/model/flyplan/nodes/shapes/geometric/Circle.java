package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;

/**
 * Created by adigu on 03.02.2017.
 */

public class Circle extends GeometricShape {

    public Circle(Coordinate coordinate) {
        super(coordinate);
    }

    @Override
    public void draw(Canvas canvas) {
        Coordinate centralizedCoordinate = centralizedCoordinate(getCoordinate(), getSize());
        Coordinate cartesianCoordinate = getScaledCoordinate(centralizedCoordinate, FlyPlanController.getInstance().getScaleFactor());

        //paint.setColor(Color.GREEN);
        //paint.setStyle(Paint.Style.FILL);
        //canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), getSize() + getBorder(), paint);

        Paint paint = new Paint(FlyPlanController.getInstance().getPaintNode());
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), getSize(), paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#CC5386E4"));
        paint.setStrokeWidth(20.0f);
        canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), getSize(), paint);
    }

    public Coordinate getScaledCoordinate(Coordinate originalPoint, float scalingFactor) {
        float newX, newY;
        newX = originalPoint.getX() / scalingFactor;
        newY = originalPoint.getY() / scalingFactor;
        return new Coordinate(newX, newY);
    }

}
