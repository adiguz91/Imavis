package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Circle;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric.GeometricShape;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Square;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.simple.Line;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.simple.Text;

import java.util.List;

/**
 * Created by adigu on 03.02.2017.
 */

public class Waypoint<T> extends Node implements IWaypointDraw {

    private GeometricShape shape;

    public Waypoint(GeometricShape shape, WaypointData data) {
        super(shape, data);
    }

    /*
    @Override
    public GeometricShape getShape() {
        return shape;
    }
    */

    GeometricShape createShape(Class<T> classType, Coordinate coordinate) {
        GeometricShape geometricShape = null;
        if(classType == Circle.class)
            geometricShape = new Circle(Waypoint.class, coordinate, CShape.WAYPOINT_CIRCLE_RADIUS);
        if(classType == Square.class)
            geometricShape =  new Square(Waypoint.class, coordinate);
        return geometricShape;
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
        Text text = new Text<Waypoint>(Waypoint.class, getShape().getCoordinate(), content);
        text.draw(canvas);
    }

    @Override
    public void addLine(Canvas canvas, Waypoint lastWaypoint, Waypoint currentWaypoint) {
        Line line = new Line(lastWaypoint.getShape().getCoordinate(), currentWaypoint.getShape().getCoordinate()) ;
        line.draw(canvas);
    }

    private float toScaleFactor(float value, float scaleFactor) {
        return value / scaleFactor;
    }

    @Override
    public void addDirection(Canvas canvas, Waypoint currentWaypoint, Waypoint nextWaypoint) {
        Paint paint = new Paint();
        final RectF rect = new RectF();
        float angleDirection = angleBetweenPoints(currentWaypoint.getShape(), nextWaypoint.getShape());
        //angleDirection = toScaleFactor(angleDirection, FlyPlanController.getInstance().getScaleFactor());
        float angleDistance = CShape.WAYPOINT_DIRECTION_ANGLE_DISTANCE;
        //angleDistance = toScaleFactor(angleDistance, FlyPlanController.getInstance().getScaleFactor());
        float anglePoint1 = angleDirection - (angleDistance/2);
        //anglePoint1 = toScaleFactor(anglePoint1, FlyPlanController.getInstance().getScaleFactor());
        //float anglePoint2 = angleDirection + (angleDistance/2);

        float radius = CShape.WAYPOINT_CIRCLE_RADIUS; //currentWaypoint.getShape()<Waypoint>.getRadius();
        //radius = toScaleFactor(radius, FlyPlanController.getInstance().getScaleFactor());
        float distance = radius + currentWaypoint.getShape().getBorder() + CShape.WAYPOINT_DIRECTION_DISTANCE;
        //distance = toScaleFactor(distance, FlyPlanController.getInstance().getScaleFactor());

        //paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.parseColor(CColor.WAYPOINT_DIRECTION));

        Coordinate scaledCurrentWaypoint;
        scaledCurrentWaypoint = currentWaypoint.getShape().getCoordinate().toScaleFactor(
                                FlyPlanController.getInstance().getScaleFactor());
        //scaledCurrentWaypoint = currentWaypoint.getShape().getCoordinate();

        //currentWaypoint.getShape().setCoordinate(scaledCurrentWaypoint);

        rect.set(scaledCurrentWaypoint.getX() - distance,
                scaledCurrentWaypoint.getY() - distance,
                scaledCurrentWaypoint.getX() + distance,
                scaledCurrentWaypoint.getY() + distance);

        Coordinate peakPoint = pointOnCircle(scaledCurrentWaypoint, distance+distance/2, angleDirection); // Kreuzpunkt
        Coordinate point1 = pointOnCircle(scaledCurrentWaypoint, distance, anglePoint1);
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
