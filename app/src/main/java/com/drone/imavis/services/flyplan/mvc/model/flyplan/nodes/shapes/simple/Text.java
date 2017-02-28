package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.simple;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CText;
import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.IShape;

/**
 * Created by adigu on 24.02.2017.
 */

public class Text<T> implements IShape {

    public Text(Class<T> type, Coordinate coordinate, String content) {
        this.coordinate = coordinate;
        this.content = content;
        this.type = type;
        this.size = CText.SIZE;

        if(type == Waypoint.class) {
            this.size = CText.SIZE;
            this.color = Color.parseColor(CColor.WAYPOINT_TEXT);
        }
        if(type == PointOfInterest.class) {
            this.size = CText.SIZE;
            this.color = Color.parseColor(CColor.POI_CIRCLE_TEXT);
        }
        if(type == int.class) {
            this.size = CText.ID_SIZE;
            this.color = Color.parseColor(CColor.WAYPOINT_TEXT);
        }
    }

    public void setTextColor(int color) {
        this.color = color;
    }

    public void setTextSize(int size) {
        this.size = size;
    }

    private Paint getPaint() {
        if(paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(color);
            paint.setTextSize(size);
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

    public void draw(Canvas canvas, boolean scaled) {
        Coordinate coordinate;
        if(scaled)
            coordinate = centralizedCoordinate().toScaleFactor(FlyPlanController.getInstance().getScaleFactor());
        else
            coordinate = centralizedCoordinate();
        canvas.drawText(content, coordinate.getX(), coordinate.getY(), getPaint());
    }

    private Coordinate centralizedCoordinate() {
        //float textWidth = paint.measureText(content) / 2;
        float centeredHeight = getPaint().getTextSize()/2 - 6;
        return new Coordinate(coordinate.getX(), coordinate.getY() + centeredHeight);
    }
}
