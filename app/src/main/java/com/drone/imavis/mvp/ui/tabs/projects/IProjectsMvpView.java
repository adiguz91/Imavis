package com.drone.imavis.mvp.ui.tabs.projects;

import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.ui.base.IMvpView;

import java.util.List;

/**
 * Created by adigu on 10.05.2017.
 */

public interface IProjectsMvpView extends IMvpView {

    void showProjects(List<Project> projects);

    void showProjectsEmpty();

    void showError();

    void onDeleteSuccess(Project project);

    void onDeleteFailed();

    void onEditSuccess(int position, Project project);

    void onEditFailed();

}