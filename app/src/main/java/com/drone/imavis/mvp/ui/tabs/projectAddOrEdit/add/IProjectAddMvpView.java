package com.drone.imavis.mvp.ui.tabs.projectAddOrEdit.add;

import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.ui.base.IMvpView;

/**
 * Created by adigu on 27.07.2017.
 */

public interface IProjectAddMvpView extends IMvpView {

    void onAddSuccess(Project project);

    void onAddFailed();
}
