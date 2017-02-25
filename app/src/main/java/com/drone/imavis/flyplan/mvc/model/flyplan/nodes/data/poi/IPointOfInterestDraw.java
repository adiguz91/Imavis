package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.poi;

import android.graphics.Canvas;

import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;

/**
 * Created by adigu on 25.02.2017.
 */

public interface IPointOfInterestDraw {
    void addText(Canvas canvas, String content);
}
