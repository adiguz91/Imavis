package com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint;

import android.graphics.Canvas;

import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.mvp.util.doublelinkedlist.DoublyLinkedList;
import com.google.gson.Gson;

import java.util.ListIterator;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Waypoints extends DoublyLinkedList<Waypoint> {

    private static Gson gson = new Gson();
    private Waypoint selectedWaypoint;
    private boolean isClosed = false;

    public Waypoints() {
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public void Load(String waypointsJSON) {
        if (!this.isEmpty())
            this.clear();

        Waypoints deserializedPOIs = gson.fromJson(waypointsJSON, Waypoints.class);
        this.addAll(deserializedPOIs);
    }

    public void drawWaypoint(Canvas canvas, Waypoint waypointLastNode, Waypoint waypoint) {
        if (waypointLastNode != null) {
            waypoint.addLine(canvas, waypointLastNode, waypoint);
            waypoint.drawProgressiveCircles(canvas, waypointLastNode.getShape(), waypoint.getShape());
            if (waypointLastNode == FlyPlanController.getInstance().getSelectedWaypoint()) {
                waypointLastNode.addRectWithTextOnLine(canvas, waypointLastNode, waypoint, "10m/s");
            }

            PointOfInterest poi = ((WaypointData) waypointLastNode.getData()).getPoi();
            if (poi != null)
                waypoint.addDirection(canvas, waypointLastNode, poi);
            else
                waypoint.addDirection(canvas, waypointLastNode, waypoint);
        }
    }

    public int draw(Canvas canvas) {
        Waypoint waypoint, waypointLastNode = null;
        ListIterator<Waypoint> iterator;

        // draw lines and directions
        iterator = this.listIterator();
        while (iterator.hasNext()) {
            waypoint = iterator.next();
            drawWaypoint(canvas, waypointLastNode, waypoint);

            if (waypoint == this.getLast()) {
                PointOfInterest poi = ((WaypointData) waypoint.getData()).getPoi();
                if (poi != null)
                    waypoint.addDirection(canvas, waypoint, poi);

                if (isClosed && this.size() >= 3) {
                    waypointLastNode = waypoint;
                    waypoint = this.getFirst();
                    drawWaypoint(canvas, waypointLastNode, waypoint);
                }
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
            if (waypoint != FlyPlanController.getInstance().getSelectedWaypoint()) {
                waypoint.draw(canvas, String.valueOf(counter), counter);
            } else
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
