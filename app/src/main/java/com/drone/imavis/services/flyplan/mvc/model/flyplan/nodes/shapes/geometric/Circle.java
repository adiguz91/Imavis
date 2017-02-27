package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;

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
        draw(canvas, false);
    }
    public void draw(Canvas canvas, boolean selected) {
        Coordinate cartesianCoordinate = getCoordinate().toScaleFactor(FlyPlanController.getInstance().getScaleFactor());
        canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), radius, getPaintCircle());
        canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), radius, getPaintCircleBorder());
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
