package com.drone.imavis.flyplan.mvc.controller;

import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.model.flyplan.FlyPlan;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;

import java.io.File;

/**
 * Created by adigu on 23.02.2017.
 */

public interface IFlyPlan {
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
