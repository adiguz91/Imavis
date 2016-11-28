package com.drone.imavis.flyplan;

import com.drone.imavis.flyplan.points.PointOfInterest;
import com.drone.imavis.flyplan.points.Waypoints;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Points {
    private Waypoints waypoints;
    private List<PointOfInterest> pointOfInterests;

    public Points() {
        this.waypoints = new Waypoints();
        this.pointOfInterests = new ArrayList<PointOfInterest>();
    }


}
