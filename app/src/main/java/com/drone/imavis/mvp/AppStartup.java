package com.drone.imavis.mvp;

import android.app.Application;
import android.content.Context;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.ProjectShort;
import com.drone.imavis.mvp.di.component.ApplicationComponent;
import com.drone.imavis.mvp.di.component.DaggerApplicationComponent;
import com.drone.imavis.mvp.di.module.ApplicationModule;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by adigu on 10.05.2017.
 */

public class AppStartup extends Application {

    //@Inject
    //DataManager mDataManager;

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Iconify.with(new FontAwesomeModule());

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
