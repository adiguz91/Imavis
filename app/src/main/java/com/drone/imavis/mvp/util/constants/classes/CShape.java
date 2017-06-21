package com.drone.imavis.mvp.util.constants.classes;


import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Circle;

/**
 * Created by adigu on 03.02.2017.
 */

public class CShape {
    public static final Class WAYPOINT_SHAPE_TYPE = Circle.class;
    public static final int WAYPOINT_CIRCLE_RADIUS = 50; // PIXELS
    public static final int WAYPOINT_CIRCLE_ID_RADIUS = 20;
    public static final int WAYPOINT_CIRCLE_BORDERSIZE = 20;
    public static final int WAYPOINT_CIRCLE_ID_BORDERSIZE = 10;
    public static final int WAYPOINT_DIRECTION_ANGLE_DISTANCE = 60;
    public static final int WAYPOINT_DIRECTION_DISTANCE = 0;
    public static final int WAYPOINT_DIRECTION_HEIGHT = 24;
    public static final int WAYPOINT_CIRCLE_ID_DISTANCE = 0;

    public static final Class POI_SHAPE_TYPE = Circle.class;
    public static final int POI_CIRCLE_RADIUS = 60;
    public static final int POI_CIRCLE_BORDERSIZE = 24;

    public static final int LINE_STROKEWIDTH = 10;
}
