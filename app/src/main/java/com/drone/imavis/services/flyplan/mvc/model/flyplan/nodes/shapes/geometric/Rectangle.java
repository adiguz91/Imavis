package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.extensions.dimension.Size;

/**
 * Created by adigu on 01.03.2017.
 */

public class Rectangle extends GeometricShape  {

    private Size size;
    private int padding;

    public Rectangle(Class classNode, Coordinate coordinateTouched, Size size, int padding) {
        super(classNode, coordinateTouched);
        this.size = size;
        this.padding = padding;
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(getBackgroundColor());
        return paint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getRect(), getPaint());
    }

    public Rect getRect() {
        Coordinate scaledCoordinate = centralizedCoordinate().toScaleFactor(FlyPlanController.getInstance().getScaleFactor());
        return new Rect((int)scaledCoordinate.getX()-size.getWidth()/2-padding,(int)scaledCoordinate.getY()-size.getHeight()/2-padding,
                (int)scaledCoordinate.getX()+size.getWidth()/2+padding,
                (int)scaledCoordinate.getY()+size.getHeight()/2+padding);
    }

    private Coordinate centralizedCoordinate() {
        //float textWidth = paint.measureText(content) / 2;
        float centeredHeight = -6;
        return new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY() + centeredHeight);
    }
}
