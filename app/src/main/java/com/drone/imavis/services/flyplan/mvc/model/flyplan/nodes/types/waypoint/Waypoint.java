package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.extensions.flyplan.math.FlyPlanMath;
import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.GeometricShape;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.simple.Line;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.simple.Text;

/**
 * Created by adigu on 03.02.2017.
 */

public class Waypoint<T> extends Node implements IWaypointDraw {

    //private GeometricShape shape;
    private Paint paint;

    public Waypoint(Coordinate touchedCoordinate) {
        super(Waypoint.class, touchedCoordinate);
        //this.shape = createShape(CShape.WAYPOINT_SHAPE_TYPE, touchedCoordinate);
    }

    /*
    @Override
    public GeometricShape getShape() {
        return shape;
    }
    */

    public Paint getPaint() {
        if(paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor(CColor.WAYPOINT_CIRCLE));
            paint.setStyle(Paint.Style.FILL);
        }   return paint;
    }

    @Override
    public void addText(Canvas canvas, String content) {
        Text<Waypoint> text = new Text<Waypoint>(Waypoint.class, getShape().getCoordinate(), content);
        text.draw(canvas);
    }

    @Override
    public void addLine(Canvas canvas, Waypoint lastWaypoint, Waypoint currentWaypoint) {
        Line line = new Line(lastWaypoint.getShape().getCoordinate(), currentWaypoint.getShape().getCoordinate()) ;
        line.draw(canvas);
    }

    @Override
    public void addDirection(Canvas canvas, Waypoint currentWaypoint, Waypoint nextWaypoint) {
        FlyPlanMath.getInstance().addDirection(canvas, currentWaypoint, nextWaypoint);
    }

    @Override
    public void draw(Canvas canvas, String content) {
        draw(canvas, content, false);
    }

    public void draw(Canvas canvas, String content, boolean selected) {
        this.getShape().draw(canvas, selected);
        addText(canvas, content);
    }

}
