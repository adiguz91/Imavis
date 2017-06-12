package com.drone.flyplanner.di.component;

/**
 * Created by adigu on 10.05.2017.
 */

import com.drone.flyplanner.di.PerActivity;
import com.drone.flyplanner.di.module.ActivityModule;
import com.drone.flyplanner.ui.flyplan.FlyPlanView;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(FlyPlanView flyPlanView);

}