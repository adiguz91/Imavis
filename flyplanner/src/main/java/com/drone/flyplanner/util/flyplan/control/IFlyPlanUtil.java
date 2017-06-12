package com.drone.flyplanner.util.flyplan.control;

import android.graphics.Canvas;

import com.drone.flyplanner.data.model.flyplan.FlyPlan;
import com.drone.flyplanner.data.model.flyplan.nodes.Node;
import com.drone.flyplanner.data.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.flyplanner.data.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.flyplanner.util.models.coordinates.Coordinate;

import java.io.File;

/**
 * Created by adigu on 23.02.2017.
 */

public interface IFlyPlanUtil {
    FlyPlan getFlyPlan();
    FlyPlan onPlanCreateNew();
    FlyPlan onPlanLoad(File file);
    boolean onPlanSave();
    boolean onPlanDelete();

    boolean onNodeAdd(Coordinate coordinate);
    Node onNodeSelect(Coordinate coordinate);
    boolean onNodeDelete(Node node);
    boolean onNodeSelectAction(Node node);

    Coordinate isTouchedTextRect(Coordinate touchCoordinate);
    boolean obtainTouchedNode(Class className, Coordinate touchCoordinate, Node touchedNode);
    float getScaleFactor();
    Node getTouchedNode();
    Node getTouchedNode(Coordinate coordinate);
    Waypoint getSelectedWaypoint();
    PointOfInterest getSelectedPOI();
    void draw(Canvas canvas);
}
