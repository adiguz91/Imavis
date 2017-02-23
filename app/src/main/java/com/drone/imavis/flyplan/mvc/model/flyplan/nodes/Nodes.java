package com.drone.imavis.flyplan.mvc.model.flyplan.nodes;

import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.poi.PointOfInterest;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.poi.PointOfInterests;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoints;

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
