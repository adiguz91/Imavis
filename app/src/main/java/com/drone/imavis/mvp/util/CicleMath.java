package com.drone.imavis.mvp.util;

import android.graphics.Point;

/**
 * Created by adigu on 08.02.2018.
 */

public final class CicleMath {

    private CicleMath() {
        throw new UnsupportedOperationException();
    }

    public static boolean isInsideCircle(Point centerPoint, float centerPointRadius, Point point) {
        // Compare radius of circle with distance of its center from
        // given point
        if ((point.x - centerPoint.x)*(point.x - centerPoint.x) +
                (point.y - centerPoint.y)*(point.y - centerPoint.y) <= centerPointRadius*centerPointRadius)
            return true;
        else
            return false;
    }

    // https://codepen.io/enxaneta/pen/dXGEbv
    public static Point getRandomPointFromCircle(Point centerPoint, double centerPointRadius) {
        double angle = Math.random()*Math.PI*2;
        double r = Math.sqrt(Math.floor(Math.random()*centerPointRadius*centerPointRadius));

        double x = centerPoint.x + r*Math.cos(angle);
        double y = centerPoint.y + r*Math.sin(angle);

        //int x = Math.cos(angle) * radius;
        //int y = Math.sin(angle) * radius;
        return new Point((int)Math.round(x), (int)Math.round(y));
    }
}
