package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by adigu on 03.02.2017.
 */

public class Circle<T> extends GeometricShape {



    public Circle(Class<T> type, Coordinate coordinate, int radius) {
        super(type, coordinate);
        this.type = type;
        this.radius = radius;
    }

    private Class<T> type;
    public Class<T> getType() { return type; }

    /*
    private void setTypeAtRuntime() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).
                getActualTypeArguments()[0];
    }
    */

    @Override
    public void draw(Canvas canvas) {
        Coordinate centralCoordinate = centralizedCoordinate(getCoordinate());
        Coordinate cartesianCoordinate = centralCoordinate.toScaleFactor(FlyPlanController.getInstance().getScaleFactor());
        canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), radius, getPaintCircle());
        canvas.drawCircle(cartesianCoordinate.getX(), cartesianCoordinate.getY(), radius, getPaintCircleBorder());
    }

    public Coordinate centralizedCoordinate(Coordinate coordinate) {
        float centeredX = coordinate.getX() - radius/2;
        float centeredY = coordinate.getY() - radius/2;
        if(centeredX < 0)
            centeredX = 0;
        if(centeredY < 0)
            centeredY = 0;
        return new Coordinate(centeredX, centeredY);
    }

    public Paint getPaintCircle() {
        if(paintCircle == null) {
            paintCircle = new Paint();
            paintCircle.setStyle(Paint.Style.FILL);
            paintCircle.setAntiAlias(true);
            paintCircle.setColor(getBackgroundColor());
        }
        return paintCircle;
    }

    public Paint getPaintCircleBorder() {
        if(paintCircleBorder == null) {
            paintCircleBorder = new Paint();
            paintCircleBorder.setStyle(Paint.Style.STROKE);
            paintCircleBorder.setColor(getBorderColor());
            paintCircleBorder.setStrokeWidth(getBorder());
        }
        return paintCircleBorder;
    }

    public float getRadius() {
        return radius;
    }

    protected void setRadius(float radius) {
        this.radius = radius;
    }

    private float radius;
    private Paint paintCircle;
    private Paint paintCircleBorder;

}
