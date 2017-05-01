package com.drone.imavis.services.flyplan.mvc.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.drone.imavis.util.constants.classes.CColor;
import com.drone.imavis.util.constants.classes.CShape;
import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.extensions.dimension.Size;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.GeometricShape;

/**
 * Created by adigu on 26.02.2017.
 */

public class FlyPlanMath {

    private static FlyPlanMath flyPlanMath;

    public FlyPlanMath() {}

    // SINGLETON PATTERN
    public static FlyPlanMath getInstance() {
        if (flyPlanMath == null)
            flyPlanMath = new FlyPlanMath();
        return flyPlanMath;
    }

    /************************************
     * begin: NODE MATH functions
     */
    public float angleBetweenPoints(GeometricShape nodeA, GeometricShape nodeB) {
        float angleInDegrees = (float) (Math.atan2(nodeB.getCoordinate().getY() - nodeA.getCoordinate().getY(),
                nodeB.getCoordinate().getX() - nodeA.getCoordinate().getX()) * 180f / Math.PI);
        if(angleInDegrees < 0)
            angleInDegrees += 360;
        return angleInDegrees;
    }

    public Size getPointOfText(String content, int textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        //width =  paint.measureText(content, 0, content.length());
        Rect bounds = new Rect();
        paint.getTextBounds(content,0,content.length(),bounds);
        return new Size(bounds.width(), bounds.height());
    }

    public Coordinate pointOnCircle(Coordinate coordinate, float radius, float angleInDegrees) {
        // Convert from degrees to radians via multiplication by PI/180
        float x = (float)(radius * Math.cos(angleInDegrees * Math.PI / 180f)) + coordinate.getX();
        float y = (float)(radius * Math.sin(angleInDegrees * Math.PI / 180f)) + coordinate.getY();
        return new Coordinate(x, y);
    }

    public float distanceOfTwoPoints(Coordinate first, Coordinate second) {
        int exponent= 2;
        double basisFirst = first.getX() - second.getX();
        double basisSecond = first.getY() - second.getY();
        double distance = Math.sqrt(
                            Math.pow(basisFirst, exponent) + Math.pow(basisSecond, exponent));
        return (float) distance;
    }

    public void addDirection(Canvas canvas, GeometricShape currentWaypoint, GeometricShape nextWaypoint, float radius) {
        Paint paint = new Paint();
        final RectF rect = new RectF();
        float angleDirection = angleBetweenPoints(currentWaypoint, nextWaypoint);
        //angleDirection = toScaleFactor(angleDirection, FlyPlanController.getInstance().getScaleFactor());
        float angleDistance = CShape.WAYPOINT_DIRECTION_ANGLE_DISTANCE;
        //angleDistance = toScaleFactor(angleDistance, FlyPlanController.getInstance().getScaleFactor());
        float anglePoint1 = angleDirection - (angleDistance/2);
        //anglePoint1 = toScaleFactor(anglePoint1, FlyPlanController.getInstance().getScaleFactor());
        //float anglePoint2 = angleDirection + (angleDistance/2);

        // radius = CShape.WAYPOINT_CIRCLE_RADIUS; //currentWaypoint.getShape()<Waypoint>.getRadius();
        //radius = toScaleFactor(radius, FlyPlanController.getInstance().getScaleFactor());
        float distance = radius + currentWaypoint.getBorder() + CShape.WAYPOINT_DIRECTION_DISTANCE;
        //distance = toScaleFactor(distance, FlyPlanController.getInstance().getScaleFactor());

        //paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.parseColor(CColor.WAYPOINT_DIRECTION));

        Coordinate scaledCurrentWaypoint;
        scaledCurrentWaypoint = currentWaypoint.getCoordinate().toScaleFactor(
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
    /*
     * end: NODE MATH functions
     ************************************/

}
