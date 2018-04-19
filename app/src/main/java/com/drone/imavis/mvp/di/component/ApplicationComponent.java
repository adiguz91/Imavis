package com.drone.imavis.mvp.di.component;

import android.app.Application;
import android.content.Context;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.SyncService;
import com.drone.imavis.mvp.data.local.db.DatabaseHelper;
import com.drone.imavis.mvp.data.local.db.DatabaseOpenHelper;
import com.drone.imavis.mvp.data.local.db.IDatabaseHelper;
import com.drone.imavis.mvp.data.local.preference.PreferencesHelper;
import com.drone.imavis.mvp.data.remote.webodm.IWebOdmApiEndpoint;
import com.drone.imavis.mvp.di.ApplicationContext;
import com.drone.imavis.mvp.di.module.ApplicationModule;
import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.view.listener.ScaleListener;
import com.drone.imavis.mvp.util.DialogUtil;
import com.drone.imavis.mvp.util.FileUtil;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;

/**
 * Created by adigu on 10.05.2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    //void inject(); // void inject(SyncService syncService);


    @ApplicationContext Context context();
    Application application();
    IWebOdmApiEndpoint webOdmService();
    PreferencesHelper preferencesHelper();
    //DatabaseHelper databaseHelper();

    //DatabaseOpenHelper databaseOpenHelper();
    DataManager dataManager();
    //FlyPlanController flyplanController();
    FileUtil fileUtil();
    //RxEventBus eventBus();
    //IFlyPlanUtil flyPlanUtil();
    //FileUtil fileUtil();
    ScaleListener scaleListener();
}