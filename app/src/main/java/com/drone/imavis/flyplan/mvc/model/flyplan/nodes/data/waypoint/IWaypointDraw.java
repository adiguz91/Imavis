package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint;

import android.graphics.Canvas;

import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.simple.Text;

/**
 * Created by adigu on 24.02.2017.
 */

public interface IWaypointDraw {
    void addText(Canvas canvas, String content);
    void addLine(Canvas canvas, Waypoint lastWaypoint, Waypoint currentWaypoint);
    void addDirection(Canvas canvas, Waypoint currentWaypoint, Waypoint nextWaypoint);
}
