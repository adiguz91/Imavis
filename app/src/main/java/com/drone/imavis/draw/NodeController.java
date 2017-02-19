package com.drone.imavis.draw;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.flyplan.coordinates.Coordinate;
import com.drone.imavis.flyplan.FlyPlan;
import com.drone.imavis.flyplan.Map;
import com.drone.imavis.flyplan.dimension.Size;
import com.drone.imavis.flyplan.nodes.Node;
import com.drone.imavis.flyplan.nodes.Node;
import com.drone.imavis.flyplan.nodes.data.poi.PointOfInterest;
import com.drone.imavis.flyplan.nodes.data.waypoint.Waypoint;

/**
 * Created by adigu on 29.01.2017.
 */

public class NodeController {

    public NodeController() {
        Coordinate mapCoordinate = new Coordinate(0,0);
        Size mapSize = new Size(50, 50);
        Map map = new Map(mapCoordinate, mapSize);
        this.flyPlan = new FlyPlan(map);
    }

    public NodeController(Map map) {
        this.flyPlan = new FlyPlan(map);
    }

    public NodeController(FlyPlan flyPlan) {
        this.flyPlan = flyPlan;
    }



    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        //paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
    }



    private FlyPlan flyPlan;
    private Paint paint;
}
