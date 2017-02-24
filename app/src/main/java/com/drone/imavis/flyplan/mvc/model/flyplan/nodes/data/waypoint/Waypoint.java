package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric.GeometricShape;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.simple.Line;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.simple.Text;

import java.util.List;

/**
 * Created by adigu on 03.02.2017.
 */

public class Waypoint extends Node implements IWaypointDraw {

    public Waypoint(GeometricShape shape, WaypointData data) {
        super(shape, data);
    }

    // first and last waypoint have bigger size then the rest
    public void addWaypointAutoSize() {

    }

    // first and last waypoint have bigger size then the rest
    public void removeWaypointAutoSize() {

    }

    public Paint getPaint() {
        if(paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor(CColor.WAYPOINT_CIRCLE));
            paint.setStyle(Paint.Style.FILL);
        }
        return paint;
    }

    private Paint paint;

    private float angleBetweenPoints(GeometricShape nodeA, GeometricShape nodeB) {
        float angleInDegrees = (float) (Math.atan2(nodeB.getCoordinate().getY() - nodeA.getCoordinate().getY(),
                nodeB.getCoordinate().getX() - nodeA.getCoordinate().getX()) * 180f / Math.PI);
        if(angleInDegrees < 0)
            angleInDegrees += 360;
        return angleInDegrees;
    }

    private Coordinate pointOnCircle(Coordinate coordinate, float radius, float angleInDegrees) {
        // Convert from degrees to radians via multiplication by PI/180
        float x = (float)(radius * Math.cos(angleInDegrees * Math.PI / 180f)) + coordinate.getX();
        float y = (float)(radius * Math.sin(angleInDegrees * Math.PI / 180f)) + coordinate.getY();
        return new Coordinate(x, y);
    }

    @Override
    public void addText(Canvas canvas, String content) {
        Text text = new Text<Waypoint>(getShape().getCoordinate(), content);
        text.draw(canvas);
    }

    @Override
    public void addLine(Canvas canvas, Waypoint lastWaypoint, Waypoint currentWaypoint) {
        Line line = new Line(lastWaypoint.getShape().getCoordinate(), currentWaypoint.getShape().getCoordinate()) ;
        line.draw(canvas);
    }

    @Override
    public void addDirection(Canvas canvas, Waypoint currentWaypoint, Waypoint nextWaypoint) {
        Paint paint = new Paint();
        final RectF rect = new RectF();
        float angleDirection = angleBetweenPoints(currentWaypoint.getShape(), nextWaypoint.getShape());
        float angleDistance = CShape.WAYPOINT_DIRECTION_ANGLE_DISTANCE;

        float anglePoint1 = angleDirection - (angleDistance/2);
        //float anglePoint2 = angleDirection + (angleDistance/2);

        float radius = CShape.WAYPOINT_CIRCLE_SIZE / 2; //currentWaypoint.getShape()<Waypoint>.getRadius();
        float distance = radius + currentWaypoint.getShape().getBorder() + CShape.WAYPOINT_DIRECTION_DISTANCE;

        //paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.parseColor(CColor.WAYPOINT_DIRECTION));

        rect.set(currentWaypoint.getShape().getCoordinate().getX() - distance,
                currentWaypoint.getShape().getCoordinate().getY() - distance,
                currentWaypoint.getShape().getCoordinate().getX() + distance,
                currentWaypoint.getShape().getCoordinate().getY() + distance);

        Coordinate peakPoint = pointOnCircle(currentWaypoint.getShape().getCoordinate(), distance+distance/2, angleDirection); // Kreuzpunkt
        Coordinate point1 = pointOnCircle(currentWaypoint.getShape().getCoordinate(), distance, anglePoint1);
        //Coordinate point2 = pointOnCircle(currentWaypoint.getShape().getCoordinate(), distance, anglePoint2);

        Path path = new Path();
        path.addArc(rect, anglePoint1, angleDistance);
        //path.moveTo(point2.getX(), point2.getY());
        //path.lineTo(shape.getCoordinate().getX(), shape.getCoordinate().getY()-distance-distance/2);
        path.lineTo(peakPoint.getX(), peakPoint.getY());
        path.lineTo(point1.getX(), point1.getY());

        //canvas.drawArc(rect, 240, 60, false, paint);
        canvas.drawPath(path, paint);
    }
}
