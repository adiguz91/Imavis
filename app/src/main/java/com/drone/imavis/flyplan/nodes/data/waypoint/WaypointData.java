package com.drone.imavis.flyplan.nodes.data.waypoint;

import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.flyplan.nodes.NodeData;
import com.drone.imavis.flyplan.nodes.data.poi.PointOfInterestData;

/**
 * Created by Adrian on 26.11.2016.
 */

public class WaypointData extends NodeData {

    public WaypointData() {}

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

    public PointOfInterestData getPoi() {
        return poi;
    }

    public void setPoi(PointOfInterestData poi) {
        this.poi = poi;
    }

    //int id;
    int direction;
    int distanceToNextNode;
    int speedToNextNode = CFlyPlan.MIN_SPEED;
    int flyHeight = CFlyPlan.MIN_FLY_HEIGHT;
    WaypointMode mode = WaypointMode.Progressive;
    PointOfInterestData poi; // or id reference to POI
}
