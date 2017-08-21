package com.drone.imavis.mvp.ui.flyplanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.appyvet.rangebar.IRangeBarFormatter;
import com.appyvet.rangebar.RangeBar;
import com.dd.processbutton.iml.ActionProcessButton;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.base.BaseFragment;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.FlyplannerFragment;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map.GoogleMapFragment;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.google.android.gms.maps.MapView;
import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by adigu on 30.05.2017.
 */

public class FlyplannerActivity extends BaseActivity implements IFlyplannerActivity { // FragmentActivity

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject FlyplannerPresenter flyplannerPresenter;
    Context context;
    FlyPlan flyplan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_flyplanner);
        ButterKnife.bind(this);
        context = this;

        flyplannerPresenter.attachView(this);
        flyplan = (FlyPlan) getIntent().getParcelableExtra("Flyplan");

        //getActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FABProgressCircle fabProgressCircleStart = (FABProgressCircle) findViewById(R.id.flyplanner_fab_pc_start);
        fabProgressCircleStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabProgressCircleStart.show();
                // todo start the drone to fly
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        fabProgressCircleStart.beginFinalAnimation();
                    }
                }, 5 * 1000);
            }
        });

        FloatingActionButton fabStart = (FloatingActionButton) findViewById(R.id.flyplanner_fab_start);
        fabStart.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_play)
                .colorRes(R.color.icons)
                .actionBarSize());

        fabProgressCircleStart.attachListener(new FABProgressListener() {
            @Override
            public void onFABProgressAnimationEnd() {
                Toast.makeText(FlyplannerActivity.this, "Finished Drone", Toast.LENGTH_LONG).show();
            }
        });

        // todo: init flyplanner fragment instance

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flyplanner, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.menu_flyplanner_action_maptype).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_map)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        menu.findItem(R.id.menu_flyplanner_action_findgps).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_crosshairs)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        menu.findItem(R.id.menu_flyplanner_action_lock).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_unlock_alt)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        if (id == R.id.menu_flyplanner_action_maptype){
            //Toast.makeText(FlyplannerActivity.this, "Refresh App", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.menu_flyplanner_action_findgps){
            //Toast.makeText(FlyplannerActivity.this, "Create Text", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.menu_flyplanner_action_lock) {

            FlyPlanView flyplannerDrawer = (FlyPlanView) ((Activity) context).findViewById(R.id.flyplannerDraw);

            if (item.getTitle().equals("Lock")) {
                // unlock
                if (flyplannerDrawer != null)
                    flyplannerDrawer.setIsLocked(false);
                item.setTitle("Unlock");
                item.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_unlock_alt)
                            .colorRes(R.color.icons)
                            .actionBarSize());
            } else {
                // lock
                if (flyplannerDrawer != null)
                    flyplannerDrawer.setIsLocked(true);
                item.setTitle("Lock");
                item.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_lock)
                            .colorRes(R.color.icons)
                            .actionBarSize());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //loginPresenter.detachView();
    }

    @Override
    public void onSaveFlyplanSuccess(FlyPlan flyplan) {

    }

    @Override
    public void onSaveFlyplanFailed() {

    }

    @Override
    public void onStartFlyplanTaskSuccess(FlyPlan flyplan) {

    }

    @Override
    public void onStartFlyplanTaskFailed() {

    }

    @Override
    public void updateFlyplanNodes() {
        Toast.makeText(this, "The camera has stopped moving.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        flyplannerPresenter.saveFlyplan(flyplan);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        //FlyPlanView flyplannerDrawer = (FlyPlanView) ((Activity) context).findViewById(R.id.flyplannerDraw);
        //flyplannerDrawer.doOnTouch(ev);

        //MapView mapView = (MapView) ((Activity) context).findViewById(R.id.googleMapView);
        //mapView.onTouchEvent(ev);

        //FlyplannerFragment flyplannerFragment = (FlyplannerFragment) getSupportFragmentManager().findFragmentById(R.id.flyplanner);


        //if(flyplannerFragment.getGoogleMapFragment() != null) {
        //    flyplannerFragment.getGoogleMapFragment().getMapView().onTouchEvent(ev);
        //}

        //boolean result = super.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //boolean result = super.onTouchEvent(event);
        return false; //!result;
    }
    */
}
