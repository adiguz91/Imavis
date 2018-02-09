package com.drone.imavis.mvp.util;

import android.graphics.Point;

/**
 * Created by adigu on 08.02.2018.
 */

public final class CircleMath {

    private CircleMath() {
        throw new UnsupportedOperationException();
    }

    public static boolean isCircleCollision(Point point1, float radius, Point point2) {

        /*
        double xDif = point1.x - point2.x;
        double yDif = point1.y - point2.y;
        double distanceSquared = xDif * xDif + yDif * yDif;
        return distanceSquared < (radius + radius) * (radius + radius);
        */

        double distance = Math.hypot(point1.x - point2.x, point1.y - point2.y);
        double diameter = radius * 2;
        if (distance > diameter)
            return false; // no collision;
        else
            return true;
    }

    public static boolean isPointInsideCircle(Point point1, Point point2, float radius) {
        // Compare radius of circle with distance of its center from given point
        if ((point2.x - point1.x) * (point2.x - point1.x) +
                (point2.y - point1.y) * (point2.y - point1.y) <= radius * radius)
            return true;
        else
            return false;
    }

    // https://codepen.io/enxaneta/pen/dXGEbv
    public static Point getRandomPointFromCircle(Point centerPoint, double radius) {
        double angle = Math.random() * Math.PI * 2;
        double r = Math.sqrt(Math.abs(Math.floor(Math.random() * radius * radius)));
        double x = centerPoint.x + r * Math.cos(angle);
        double y = centerPoint.y + r * Math.sin(angle);

        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    public static Point centerPoint(Point point, int radius) {
        point.set(point.x - radius, point.y - radius);
        return point;
    }
}
