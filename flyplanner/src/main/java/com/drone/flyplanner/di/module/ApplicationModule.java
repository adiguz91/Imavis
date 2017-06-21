package com.drone.flyplanner.di.module;

/**
 * Created by adigu on 10.05.2017.
 */

import android.app.Application;
import android.content.Context;

import com.drone.flyplanner.di.ApplicationContext;
import com.drone.flyplanner.util.FileUtil;
import com.drone.flyplanner.util.flyplan.control.FlyPlanUtil;
import com.drone.flyplanner.util.flyplan.control.IFlyPlanUtil;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {

    /*
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }


    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }
    */

    @Provides
    @Singleton
    IFlyPlanUtil provideFlyPlanUtil() {
        return new FlyPlanUtil();
    }

    @Provides
    @Singleton
    FileUtil provideFileUtil() {
        return new FileUtil();
    }
}