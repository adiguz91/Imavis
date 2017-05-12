package com.drone.imavis.mvp.di.component;

import android.app.Application;
import android.content.Context;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.SyncService;
import com.drone.imavis.mvp.di.ApplicationContext;
import com.drone.imavis.mvp.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by adigu on 10.05.2017.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext Context context();
    Application application();
    //RibotsService ribotsService();
    //PreferencesHelper preferencesHelper();
    //DatabaseHelper databaseHelper();
    DataManager dataManager();
    //RxEventBus eventBus();

}