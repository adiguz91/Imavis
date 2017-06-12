package com.drone.flyplanner.di.component;

import android.app.Application;
import android.content.Context;

import com.drone.flyplanner.di.ApplicationContext;
import com.drone.flyplanner.di.module.ApplicationModule;
import com.drone.flyplanner.util.FileUtil;
import com.drone.flyplanner.util.flyplan.control.IFlyPlanUtil;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by adigu on 10.05.2017.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    //void inject(SyncService syncService);

    @ApplicationContext Context context();
    Application application();
    IFlyPlanUtil flyPlanUtil();
    FileUtil fileUtil();
    //PreferencesHelper preferencesHelper();
    //DatabaseHelper databaseHelper();
    //DataManager dataManager();
    //RxEventBus eventBus();

}