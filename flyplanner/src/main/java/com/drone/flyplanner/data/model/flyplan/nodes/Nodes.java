package com.drone.flyplanner.data.model.flyplan.nodes;

import com.drone.flyplanner.data.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.flyplanner.data.model.flyplan.nodes.types.poi.PointOfInterests;
import com.drone.flyplanner.data.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.flyplanner.data.model.flyplan.nodes.types.waypoint.Waypoints;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Nodes {
    private Waypoints waypoints;
    private PointOfInterests pointOfInterests;

    public Nodes() {
        this.waypoints = new Waypoints();
        this.pointOfInterests = new PointOfInterests();
    }

    public Waypoints getWaypoints() {
        return waypoints;
    }

    public PointOfInterests getPointOfInterests() {
        return pointOfInterests;
    }

    public void editNode(Node node) {
        if(node.getClass() == Waypoint.class) {
            int nodeIndex = getWaypoints().indexOf(node);
            getWaypoints().set(nodeIndex, (Waypoint) node);
            //getWaypoints().add((Waypoint) node);
        }
        else {
            //getPointOfInterests().add((PointOfInterest) node);
        }
    }

    public void addNode(Node node) {
        if(node.getClass() == Waypoint.class) {
            getWaypoints().add((Waypoint) node);
        }
        else {
            getPointOfInterests().add((PointOfInterest) node);
        }
    }

    public void removeNode(Node node) {
        if(node.getClass() == Waypoint.class) {
            getWaypoints().remove((Waypoint) node);
        }
        else {
            getPointOfInterests().remove((PointOfInterest) node);
        }
    }
}
