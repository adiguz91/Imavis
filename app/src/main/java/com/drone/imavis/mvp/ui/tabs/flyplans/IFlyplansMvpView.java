package com.drone.imavis.mvp.ui.tabs.flyplans;

import com.drone.imavis.mvp.data.model.Flyplan;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.FlyPlan;
import com.drone.imavis.mvp.ui.base.IMvpView;

import java.util.List;

/**
 * Created by adigu on 10.05.2017.
 */

public interface IFlyplansMvpView extends IMvpView {

    void showFlyplans(List<FlyPlan> flyplanList);

    void showFlyplansEmpty();

    void showError();

    void onDeleteSuccess(FlyPlan project);

    void onDeleteFailed();

    void onEditSuccess(int position, FlyPlan project);

    void onEditFailed();

}