package com.drone.flyplanner.data.model.flyplan.nodes.shapes.geometric;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.drone.flyplanner.util.constants.classes.CMap;
import com.drone.flyplanner.util.models.coordinates.Coordinate;
import com.drone.flyplanner.util.models.dimension.Size;

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
        canvas.drawRect(getRect(CMap.SCALE_FACTOR_DEFAULT), getPaint());
    }

    @Override
    public void draw(Canvas canvas, float scaleFactor) {
        canvas.drawRect(getRect(scaleFactor), getPaint());
    }

    public Rect getRect(float scaleFactor) {
        if(scaleFactor == Float.MIN_VALUE)
            scaleFactor = CMap.SCALE_FACTOR_DEFAULT;

        Coordinate scaledCoordinate = centralizedCoordinate().toScaleFactor(scaleFactor);
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
