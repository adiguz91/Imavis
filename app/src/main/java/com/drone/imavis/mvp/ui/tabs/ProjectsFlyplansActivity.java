package com.drone.imavis.mvp.ui.tabs;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.services.dronecontrol.DroneDiscoverer;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;
import com.drone.imavis.mvp.ui.tabs.flyplans.FlyplansFragment;
import com.drone.imavis.mvp.ui.tabs.projects.ProjectsFragment;
import com.drone.imavis.mvp.util.dronecontroll.DronePermissionRequestHelper;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adigu on 23.05.2017.
 */

public class ProjectsFlyplansActivity extends BaseActivity implements ProjectsFragment.ProjectSelected, DroneDiscoverer.Listener {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.projects.ProjectsFlyplansActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject DronePermissionRequestHelper dronePermissionRequestHelper;

    //@Inject
    //ProjectsPresenter projectsPresenter;
    //private ProjectListViewAdapter projectsListViewAdapter;
    //@Inject ProjectListViewAdapter projectsListViewAdapter;

    List<ARDiscoveryDeviceService> dronesList;

    /** List of runtime permission we need. */
    private static final String[] PERMISSIONS_NEEDED = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    /** Code for permission request result handling. */
    private static final int REQUEST_CODE_PERMISSIONS_REQUEST = 1;

    public DroneDiscoverer droneDiscoverer;

    // this block loads the native libraries
    // it is mandatory
    static {
        ARSDK.loadSDKLibs();
    }

    //@BindView(R.id.projectSwipeListView)
    //ListView projectsListView;

    private Context context;
    private FragmentPagerItemAdapter fragmentAdapter;

    @BindView(R.id.viewpager)
    CustomViewPager viewPager;

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewPagerTab;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, FlyplannerActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_tabs_projects_flyplans);
        ButterKnife.bind(this);
        context = this;

        /* Drone */
        droneDiscoverer = new DroneDiscoverer(this);
        dronePermissionRequestHelper.requestPermission(PERMISSIONS_NEEDED, REQUEST_CODE_PERMISSIONS_REQUEST);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.apptoolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.apptoolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        */
        // do stuff

        fragmentAdapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Projects", ProjectsFragment.class)
                .add("Flyplans", FlyplansFragment.class)
                .create());


        viewPager.setAdapter(fragmentAdapter);
        viewPagerTab.setViewPager(viewPager);

        onSelectProjectsFragment();
        //viewPager.setPagingEnabled(false);
        //viewPagerTab.getTabAt(1).setClickable(false);

        viewPagerTab.setOnPageChangeListener(new CustomViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == 0) {
                    // projects
                    onSelectProjectsFragment();
                }
            }
        });
    }

    private void onSelectProjectsFragment() {
        viewPager.setPagingEnabled(false);
        viewPagerTab.getTabAt(1).setClickable(false);
        // change tab color disabled
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //projectsPresenter.detachView();
    }

    @Override
    public void onSendProject(Project project) {

        FlyplansFragment flyplansFragment = (FlyplansFragment) fragmentAdapter.getPage(1);
        if(flyplansFragment != null)
            flyplansFragment.loadFlyplans(project);

        viewPager.setPagingEnabled(true);
        viewPagerTab.getTabAt(1).setClickable(true);
        viewPagerTab.getTabAt(1).performClick();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.drones_spinner).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_wifi)
                        .colorRes(R.color.icons)
                        .actionBarSize());
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drones_spinner:
                //todo
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDialog(Context context, String title, List<String> list, String[] btnText,
                           DialogInterface.OnClickListener listener) {

        String[] items = new String[list.size()];
        items = list.toArray(items);

        if (listener == null)
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                }
            };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {}
                });
        builder.setPositiveButton(btnText[0], listener);
        if (btnText.length != 1) {
            builder.setNegativeButton(btnText[1], listener);
        }
        builder.show();
    }

    /* Drone Code */

    @Override
    public void onDronesListUpdated(List<ARDiscoveryDeviceService> dronesList) {

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // setup the drone discoverer and register as listener
        droneDiscoverer.setup();
        droneDiscoverer.addListener(this); // onDronesListUpdated

        // start discovering
        droneDiscoverer.startDiscovering();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // clean the drone discoverer object
        droneDiscoverer.stopDiscovering();
        //mDroneDiscoverer.cleanup();
        //mDroneDiscoverer.removeListener(mDiscovererListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean denied = false;
        if (permissions.length == 0) {
            // canceled, finish
            denied = true;
        } else {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    denied = true;
                }
            }
        }

        if (denied) {
            Toast.makeText(this, "At least one permission is missing.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
