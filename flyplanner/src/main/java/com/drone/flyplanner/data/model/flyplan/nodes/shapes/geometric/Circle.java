package com.drone.flyplanner.data.model.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.drone.flyplanner.util.constants.classes.CFlyPlan;
import com.drone.flyplanner.util.constants.classes.CMap;
import com.drone.flyplanner.util.models.coordinates.Coordinate;


/**
 * Created by adigu on 03.02.2017.
 */

public class Circle<T> extends GeometricShape {

    public Circle(Class<T> classT, Coordinate coordinate, int radius) {
        super(classT, coordinate);
        this.classT = classT;
        this.radius = radius;
    }

    /*
    private void setTypeAtRuntime() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).
                getActualTypeArguments()[0];
    }
    */

    @Override
    public void draw(Canvas canvas) {
        draw(canvas, CMap.SCALE_FACTOR_DEFAULT);
    }
    public void draw(Canvas canvas, float scaleFactor) {
        if(scaleFactor == Float.MIN_VALUE) {
            Coordinate cartesianCoordinate = getCoordinate().toScaleFactor(scaleFactor);
            canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), radius, getPaintCircle());
            canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), radius, getPaintCircleBorder());
        } else {
            canvas.drawCircle(getCoordinate().getX(), getCoordinate().getY(), radius, getPaintCircle());
            canvas.drawCircle(getCoordinate().getX(), getCoordinate().getY(), radius, getPaintCircleBorder());
        }

    }

    public Paint getPaintCircle() {
        paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setAntiAlias(true);
        paintCircle.setColor(getBackgroundColor());
        return paintCircle;
    }

    public Paint getPaintCircleBorder() {
        paintCircleBorder = new Paint();
        paintCircleBorder.setStyle(Paint.Style.STROKE);
        paintCircleBorder.setColor(getBorderColor());
        paintCircleBorder.setStrokeWidth(getBorder());
        return paintCircleBorder;
    }

    public float getRadius() {
        return radius;
    }
    protected void setRadius(float radius) {
        this.radius = radius;
    }

    private Class<T> classT;
    private float radius;
    private Paint paintCircle;
    private Paint paintCircleBorder;

}