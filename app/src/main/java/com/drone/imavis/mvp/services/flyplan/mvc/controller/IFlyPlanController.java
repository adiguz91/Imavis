package com.drone.imavis.mvp.services.flyplan.mvc.controller;

import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.mvp.ui.base.IMvpView;

import java.io.File;

/**
 * Created by adigu on 23.02.2017.
 */

public interface IFlyPlanController extends IMvpView {
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
