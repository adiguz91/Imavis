package com.drone.imavis.mvp.ui.base;

/**
 * Created by adigu on 10.05.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.drone.imavis.mvp.AppStartup;
import com.drone.imavis.mvp.R;
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
public class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(AppStartup.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    public void goToActivity(Context activity, Class nextActivity, Bundle bundleData) {
        Intent intent = new Intent(activity, nextActivity);
        intent.putExtras(bundleData); //Put your data to your next Intent
        activity.startActivity(intent);
    }

    public void goBack(Context activity) {
        ((Activity) activity).finish();
    }

    public void showToast(String message) {
        Toast toast = new Toast(this);
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setupBackButton() {
        // set backbutton color
        final Drawable leftArrow = getDrawable(R.drawable.abc_ic_ab_back_material);
        leftArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(leftArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}