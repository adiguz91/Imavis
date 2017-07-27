package com.drone.imavis.mvp.di.component;

/**
 * Created by adigu on 10.05.2017.
 */

import com.drone.imavis.mvp.di.PerActivity;
import com.drone.imavis.mvp.di.module.ActivityModule;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;
import com.drone.imavis.mvp.ui.tabs.ProjectsFlyplansActivity;
import com.drone.imavis.mvp.ui.login.LoginActivity;
import com.drone.imavis.mvp.ui.tabs.flyplans.FlyplansActivity;
import com.drone.imavis.mvp.ui.tabs.flyplans.FlyplansFragment;
import com.drone.imavis.mvp.ui.tabs.projects.ProjectsActivity;
import com.drone.imavis.mvp.ui.tabs.projects.ProjectsFragment;
import com.drone.imavis.mvp.ui.tabs.projectsAdd.ProjectAddActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LoginActivity loginActivity);

    void inject(ProjectsFlyplansActivity projectsFlyplansActivity);
    void inject(ProjectsActivity projectsActivity);
    void inject(FlyplansActivity flyPlansActivity);
    void inject(FlyplansFragment flyplansFragment);
    void inject(ProjectsFragment projectsFragment);
    void inject(FlyplannerActivity flyplannerActivity);
    void inject(ProjectAddActivity projectAddActivity);
}