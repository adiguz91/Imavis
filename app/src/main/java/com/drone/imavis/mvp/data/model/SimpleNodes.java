package com.drone.imavis.mvp.data.model;

import java.util.List;

/**
 * Created by adigu on 14.01.2018.
 */

public class SimpleNodes {
    private List<Waypoint> waypoints;
    private List<PointOfInterest> pointOfInterests;

    public SimpleNodes() {}

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public List<PointOfInterest> getPointOfInterests() {
        return pointOfInterests;
    }

    public void setPointOfInterests(List<PointOfInterest> pointOfInterests) {
        this.pointOfInterests = pointOfInterests;
    }
}
