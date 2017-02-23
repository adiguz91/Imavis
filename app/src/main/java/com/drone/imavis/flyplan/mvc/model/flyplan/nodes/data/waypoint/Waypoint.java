package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint;

import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric.GeometricShape;

/**
 * Created by adigu on 03.02.2017.
 */

public class Waypoint extends Node {

    public Waypoint(GeometricShape shape, WaypointData data) {
        super(shape, data);
    }

    // first and last waypoint have bigger size then the rest
    public void addWaypointAutoSize() {

    }

    // first and last waypoint have bigger size then the rest
    public void removeWaypointAutoSize() {

    }
}
