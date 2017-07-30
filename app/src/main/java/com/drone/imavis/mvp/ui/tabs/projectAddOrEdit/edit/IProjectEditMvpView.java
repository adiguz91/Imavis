package com.drone.imavis.mvp.ui.tabs.projectAddOrEdit.edit;

import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.ui.base.IMvpView;

/**
 * Created by adigu on 27.07.2017.
 */

public interface IProjectEditMvpView extends IMvpView {

    void onEditSuccess(Project project);

    void onEditFailed();
}
