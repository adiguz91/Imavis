package com.drone.imavis.mvp.di.module;

/**
 * Created by adigu on 10.05.2017.
 */

import android.app.Application;
import android.content.Context;

import com.drone.imavis.mvp.data.remote.webodm.IWebOdmApiEndpoint;
import com.drone.imavis.mvp.data.remote.webodm.WebOdmService;
import com.drone.imavis.mvp.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
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

    @Provides
    @Singleton
    IWebOdmApiEndpoint provideWebOdmService(WebOdmService webOdmService) {
        return webOdmService.getWebOdmService();
    }
/*
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
    */
}