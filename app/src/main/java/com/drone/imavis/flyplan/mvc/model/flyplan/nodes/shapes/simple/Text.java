package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.simple;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.constants.classes.CText;
import com.drone.imavis.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.model.extensions.dimension.Size;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.poi.PointOfInterest;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.IShape;

/**
 * Created by adigu on 24.02.2017.
 */

public class Text<T> implements IShape {

    public Text(Coordinate coordinate, String content) {
        this.coordinate = coordinate;
        this.content = content;

        if(type == Waypoint.class) {
            this.size = CText.SIZE;
            this.color = Color.parseColor(CColor.WAYPOINT_TEXT);
        }
        if(type == PointOfInterest.class) {
            this.size = CText.SIZE;;
            this.color = Color.parseColor(CColor.POI_CIRCLE_TEXT);
        }
    }

    private Paint getPaint() {
        if(paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(color);
            paint.setTextSize(CText.SIZE);
            paint.setTextAlign(Paint.Align.CENTER);
        }
        return paint;
    }

    private Coordinate coordinate;
    private String content;
    private int color;
    private int size;
    private Paint paint;
    private Class<T> type;

    @Override
    public void draw(Canvas canvas) {
        Coordinate scaledCoordinate = centralizedCoordinate().toScaleFactor(FlyPlanController.getInstance().getScaleFactor());
        canvas.drawText(content, scaledCoordinate.getX(), scaledCoordinate.getY(), getPaint());
    }

    private Coordinate centralizedCoordinate() {
        //float textWidth = paint.measureText(content) / 2;
        float textHeight = getPaint().getTextSize();
        return new Coordinate(coordinate.getX(), coordinate.getY() + textHeight / 2);
    }
}
