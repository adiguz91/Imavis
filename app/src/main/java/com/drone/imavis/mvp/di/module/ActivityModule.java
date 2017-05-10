package com.drone.imavis.mvp.di.module;

import android.app.Activity;
import android.content.Context;

import com.drone.imavis.mvp.di.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by adigu on 10.05.2017.
 */

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }
}