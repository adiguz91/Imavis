package com.drone.imavis.mvp.ui.tabs.flyplanAddOrEdit;

import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.ui.base.IMvpView;

/**
 * Created by adigu on 27.07.2017.
 */

public interface IFlyplanAddOrEditMvpView extends IMvpView {

    void onAddSuccess(FlyPlan flyPlan);

    void onAddFailed();

    void onEditSuccess(FlyPlan flyPlan);

    void onEditFailed();
}
