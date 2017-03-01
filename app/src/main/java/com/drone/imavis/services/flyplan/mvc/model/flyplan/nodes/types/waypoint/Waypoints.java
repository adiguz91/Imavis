package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Circle;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.google.gson.Gson;
import com.drone.imavis.extensions.doublelinkedlist.DoublyLinkedList;

import java.util.ListIterator;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Waypoints extends DoublyLinkedList<Waypoint> {

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
                if(waypointLastNode == FlyPlanController.getSelectedWaypoint()) {
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
            if(waypoint != FlyPlanController.getSelectedWaypoint()) {
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
