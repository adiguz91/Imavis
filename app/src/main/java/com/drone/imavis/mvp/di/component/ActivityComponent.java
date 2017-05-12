package com.drone.imavis.mvp.di.component;

/**
 * Created by adigu on 10.05.2017.
 */

import com.drone.imavis.mvp.di.PerActivity;
import com.drone.imavis.mvp.di.module.ActivityModule;
import com.drone.imavis.mvp.ui.flyplans.FlyplansActivity;
import com.drone.imavis.mvp.ui.projects.ProjectsActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(ProjectsActivity projectsActivity);
    void inject(FlyplansActivity flyPlansActivity);

}