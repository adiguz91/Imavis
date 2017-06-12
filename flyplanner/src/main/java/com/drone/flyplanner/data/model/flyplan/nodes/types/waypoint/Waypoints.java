package com.drone.flyplanner.data.model.flyplan.nodes.types.waypoint;

import android.graphics.Canvas;

import com.drone.flyplanner.data.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.flyplanner.util.doublelinkedlist.DoublyLinkedList;

import com.drone.flyplanner.util.flyplan.control.IFlyPlanUtil;
import com.google.gson.Gson;

import java.util.ListIterator;

import javax.inject.Inject;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Waypoints extends DoublyLinkedList<Waypoint> {

    @Inject
    IFlyPlanUtil flyPlanUtil;

    private static Gson gson = new Gson();
    private Waypoint selectedWaypoint;

    public Waypoints() {}

    public void Load(String waypointsJSON) {
        if(!this.isEmpty())
            this.clear();

        Waypoints deserializedPOIs = gson.fromJson(waypointsJSON, Waypoints.class);
        this.addAll(deserializedPOIs);
    }

    public int draw(Canvas canvas) {
        Waypoint waypoint, waypointLastNode = null;
        ListIterator<Waypoint> iterator;

        // draw lines and directions
        iterator = this.listIterator();
        while (iterator.hasNext()) {
            waypoint = iterator.next();
            if(waypointLastNode != null) {
                waypoint.addLine(canvas, waypointLastNode, waypoint);
                waypoint.drawProgressiveCircles(canvas, waypointLastNode.getShape(), waypoint.getShape());
                if(waypointLastNode == flyPlanUtil.getSelectedWaypoint()) {
                    waypointLastNode.addRectWithTextOnLine(canvas, waypointLastNode, waypoint, "10m/s");
                }
                
                PointOfInterest poi = ((WaypointData)waypointLastNode.getData()).getPoi();
                if(poi != null)
                    waypoint.addDirection(canvas, waypointLastNode, poi);
                else
                    waypoint.addDirection(canvas, waypointLastNode, waypoint);
            }

            if(waypoint == this.getLast()) {
                PointOfInterest poi = ((WaypointData) waypoint.getData()).getPoi();
                if(poi != null)
                    waypoint.addDirection(canvas, waypoint, poi);
            }
            waypointLastNode = waypoint;
        }

        // draw shape with text
        int counter = 1;
        int selectedWaypointIndex = -1;
        iterator = this.listIterator();
        while (iterator.hasNext()) {
            waypoint = iterator.next();
            waypoint.setShapePaint();
            if(waypoint != flyPlanUtil.getSelectedWaypoint()) {
                waypoint.draw(canvas, String.valueOf(counter), counter);
            }
            else
                selectedWaypointIndex = counter - 1;
            counter++;
        }

        return selectedWaypointIndex; // if -1 then notfound else found
    }

    public boolean Save() {
        // TODO save offline into file
        return false;
    }
}
