package com.drone.flyplanner.util.flyplan.control;

import com.drone.flyplanner.data.model.flyplan.FlyPlan;
import com.drone.flyplanner.data.model.flyplan.nodes.Node;
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
}
