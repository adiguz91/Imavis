package com.drone.imavis.mvp.services.flyplan.mvc.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.dimension.Size;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.GeometricShape;
import com.drone.imavis.mvp.util.constants.classes.CColor;
import com.drone.imavis.mvp.util.constants.classes.CShape;

/**
 * Created by adigu on 26.02.2017.
 */

public class FlyPlanMath {

    private static FlyPlanMath flyPlanMath;

    public FlyPlanMath() {
    }

    // SINGLETON PATTERN
    public static FlyPlanMath getInstance() {
        if (flyPlanMath == null)
            flyPlanMath = new FlyPlanMath();
        return flyPlanMath;
    }

    public float angleBetweenPoints(GeometricShape nodeA, GeometricShape nodeB) {
        float angleInDegrees = (float) (Math.atan2(nodeB.getCoordinate().getY() - nodeA.getCoordinate().getY(),
                nodeB.getCoordinate().getX() - nodeA.getCoordinate().getX()) * 180f / Math.PI);
        if (angleInDegrees < 0)
            angleInDegrees += 360;
        return angleInDegrees;
    }

    public Size getPointOfText(String content, int textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(content, 0, content.length(), bounds);
        return new Size(bounds.width(), bounds.height());
    }

    public Coordinate pointOnCircle(Coordinate coordinate, float radius, float angleInDegrees) {
        // Convert from degrees to radians via multiplication by PI/180
        float x = (float) (radius * Math.cos(angleInDegrees * Math.PI / 180f)) + coordinate.getX();
        float y = (float) (radius * Math.sin(angleInDegrees * Math.PI / 180f)) + coordinate.getY();
        return new Coordinate(x, y);
    }

    public float distanceOfTwoPoints(Coordinate first, Coordinate second) {
        int exponent = 2;
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
        float angleDistance = CShape.WAYPOINT_DIRECTION_ANGLE_DISTANCE;
        float anglePoint1 = angleDirection - (angleDistance / 2);
        float distance = radius + currentWaypoint.getBorder() + CShape.WAYPOINT_DIRECTION_DISTANCE;
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.parseColor(CColor.WAYPOINT_DIRECTION));

        Coordinate scaledCurrentWaypoint;
        scaledCurrentWaypoint = currentWaypoint.getCoordinate().toScaleFactor(
                FlyPlanController.getInstance().getScaleFactor());

        rect.set(scaledCurrentWaypoint.getX() - distance,
                scaledCurrentWaypoint.getY() - distance,
                scaledCurrentWaypoint.getX() + distance,
                scaledCurrentWaypoint.getY() + distance);

        Coordinate peakPoint = pointOnCircle(scaledCurrentWaypoint, distance + distance / 2, angleDirection); // Kreuzpunkt
        Coordinate point1 = pointOnCircle(scaledCurrentWaypoint, distance, anglePoint1);

        Path path = new Path();
        path.addArc(rect, anglePoint1, angleDistance);
        path.lineTo(peakPoint.getX(), peakPoint.getY());
        path.lineTo(point1.getX(), point1.getY());

        canvas.drawPath(path, paint);
    }
}
