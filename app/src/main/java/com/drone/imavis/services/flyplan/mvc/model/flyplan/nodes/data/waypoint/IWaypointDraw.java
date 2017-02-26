package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.data.waypoint;

import android.graphics.Canvas;

/**
 * Created by adigu on 24.02.2017.
 */

public interface IWaypointDraw {
    void addText(Canvas canvas, String content);
    void addLine(Canvas canvas, Waypoint lastWaypoint, Waypoint currentWaypoint);
    void addDirection(Canvas canvas, Waypoint currentWaypoint, Waypoint nextWaypoint);
}
