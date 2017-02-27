package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.simple;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.IShape;

/**
 * Created by adigu on 03.02.2017.
 */

public class Line implements IShape {

    public Line(Coordinate startCoordinate, Coordinate endCoordinate) {
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
    }

    private Paint getPaint() {
        if(paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor(CColor.LINE));
            paint.setStrokeWidth(CShape.LINE_STROKEWIDTH);
            paint.setStyle(Paint.Style.STROKE);
        }
        return paint;
    }

    private Paint paint;
    private Coordinate startCoordinate;
    private Coordinate endCoordinate;

    @Override
    public void draw(Canvas canvas) {
        Coordinate fromScaledCoordinate = startCoordinate.toScaleFactor(FlyPlanController.getInstance().getScaleFactor());
        Coordinate toScaledCoordinate = endCoordinate.toScaleFactor(FlyPlanController.getInstance().getScaleFactor());
        Path path = new Path();
        path.moveTo(fromScaledCoordinate.getX(), fromScaledCoordinate.getY());
        path.lineTo(toScaledCoordinate.getX(),toScaledCoordinate.getY());
        canvas.drawPath(path, getPaint());
    }

    @Override
    public void draw(Canvas canvas, boolean selected) {
        draw(canvas);
    }
}
