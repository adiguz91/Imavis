package com.drone.imavis.mvp.ui.base;

/**
 * Created by adigu on 10.05.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewParent;

import com.drone.imavis.mvp.AppStartup;
import com.drone.imavis.mvp.di.component.ActivityComponent;
import com.drone.imavis.mvp.di.component.ConfigPersistentComponent;
import com.drone.imavis.mvp.di.component.DaggerConfigPersistentComponent;
import com.drone.imavis.mvp.di.module.ActivityModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseFragment extends Fragment {

    private static final String KEY_ACTIVITY_ID = "KEY_FRAGMENT_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(AppStartup.get(this.getActivity()).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this.getActivity()));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    public void onDestroy() {
        //if (!isChangingConfigurations()) {
        //    Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
        //    sComponentsMap.remove(mActivityId);
        //}
        super.onDestroy();
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    public ViewParent findParentRecursively(View view, int targetId) {
        if (view.getId() == targetId)
            return (ViewParent) view;
        View parent = (View) view.getParent();
        if (parent == null)
            return null;
        return findParentRecursively(parent, targetId);
    }

    public void goToActivity(Context activity, Class nextActivity, Bundle bundleData) {
        Intent intent = new Intent(activity, nextActivity);
        intent.putExtras(bundleData); //Put your data to your next Intent
        activity.startActivity(intent);
    }


    public void goBack(Context activity) {
        ((Activity) activity).finish();
    }

}