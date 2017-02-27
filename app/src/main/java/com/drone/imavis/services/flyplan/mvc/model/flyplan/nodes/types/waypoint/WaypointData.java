package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint;

import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.NodeData;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterestData;

/**
 * Created by Adrian on 26.11.2016.
 */

public class WaypointData extends NodeData {

    public WaypointData() {
        paint = new Paint();
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDistanceToNextNode() {
        return distanceToNextNode;
    }

    public void setDistanceToNextNode(int distanceToNextNode) {
        this.distanceToNextNode = distanceToNextNode;
    }

    public int getSpeedToNextNode() {
        return speedToNextNode;
    }

    public void setSpeedToNextNode(int speedToNextNode) {
        this.speedToNextNode = speedToNextNode;
    }

    public int getFlyHeight() {
        return flyHeight;
    }

    public void setFlyHeight(int flyHeight) {
        this.flyHeight = flyHeight;
    }

    public WaypointMode getMode() {
        return mode;
    }

    public void setMode(WaypointMode mode) {
        this.mode = mode;
    }

    public PointOfInterest getPoi() {
        return poi;
    }

    public void setPoi(PointOfInterest poi) {
        this.poi = poi;
    }

    public Paint getPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        //paint.setStrokeWidth(40);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    //int id;
    private int direction;
    private int distanceToNextNode;
    private int speedToNextNode = CFlyPlan.MIN_SPEED;
    private int flyHeight = CFlyPlan.MIN_FLY_HEIGHT;
    private WaypointMode mode = WaypointMode.Progressive;
    private PointOfInterest poi; // or id reference to POI
    private Paint paint;
}
