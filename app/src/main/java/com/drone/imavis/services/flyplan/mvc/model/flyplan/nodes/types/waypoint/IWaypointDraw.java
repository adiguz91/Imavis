package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint;

import android.graphics.Canvas;

import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.Node;

/**
 * Created by adigu on 24.02.2017.
 */

public interface IWaypointDraw {
    void addLine(Canvas canvas, Waypoint lastWaypoint, Waypoint currentWaypoint);
    void addDirection(Canvas canvas, Waypoint currentWaypoint, Node nextWaypoint);
    void draw(Canvas canvas, String content, int id);
}
