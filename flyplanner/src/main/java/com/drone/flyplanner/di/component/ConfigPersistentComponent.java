package com.drone.flyplanner.di.component;

/**
 * Created by adigu on 10.05.2017.
 */

import com.drone.flyplanner.di.ConfigPersistent;
import com.drone.flyplanner.di.module.ActivityModule;

import dagger.Component;

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check {@link com.drone.imavis.mvp.ui.base.BaseActivity} to see how this components
 * survives configuration changes.
 * Use the {@link com.drone.imavis.mvp.di.ConfigPersistent} scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

    ActivityComponent activityComponent(ActivityModule activityModule);

}