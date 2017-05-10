package com.drone.imavis.mvp;

import android.app.Application;
import android.content.Context;

import com.drone.imavis.mvp.di.component.ApplicationComponent;
import com.drone.imavis.mvp.di.component.DaggerApplicationComponent;
import com.drone.imavis.mvp.di.module.ApplicationModule;

import timber.log.Timber;

/**
 * Created by adigu on 10.05.2017.
 */

public class AppStartup extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            //Fabric.with(this, new Crashlytics());
        }
    }

    public static AppStartup get(Context context) {
        return (AppStartup) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
