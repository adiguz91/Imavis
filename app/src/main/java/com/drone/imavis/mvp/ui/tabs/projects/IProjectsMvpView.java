package com.drone.imavis.mvp.ui.tabs.projects;

import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.ui.base.IMvpView;

/**
 * Created by adigu on 10.05.2017.
 */

public interface IProjectsMvpView extends IMvpView {

    void showProjects(Projects projects);

    void showProjectsEmpty();

    void showError();

}