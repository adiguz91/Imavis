package com.drone.imavis.mvp.ui.modelviewer;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.di.ConfigPersistent;
import com.drone.imavis.mvp.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by adigu on 09.09.2017.
 */

@ConfigPersistent
public class ModelViewerPresenter extends BasePresenter<IModelViewerActivity> {

    private final DataManager dataManager;

    @Inject
    public ModelViewerPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }
}
