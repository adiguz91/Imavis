package com.drone.imavis.mvp.ui.tabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;
import com.drone.imavis.mvp.ui.tabs.flyplans.FlyplansFragment;
import com.drone.imavis.mvp.ui.tabs.projects.ProjectsFragment;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by adigu on 23.05.2017.
 */

public class ProjectsFlyplansActivity extends BaseActivity implements ProjectsFragment.ProjectSelected {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.projects.ProjectsFlyplansActivity.EXTRA_TRIGGER_SYNC_FLAG";

    //@Inject
    //ProjectsPresenter projectsPresenter;
    //private ProjectListViewAdapter projectsListViewAdapter;
    //@Inject ProjectListViewAdapter projectsListViewAdapter;

    //@BindView(R.id.projectSwipeListView)
    //ListView projectsListView;

    private Context context;
    private CustomViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private FragmentPagerItemAdapter fragmentAdapter;

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

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(fragmentAdapter);

        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
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
        int id = item.getItemId();
        if (id == R.id.drones_spinner){

            // TODO discover drones!
            List<String> drones = new ArrayList<String>();
            drones.add("Bebop1");
            drones.add("Bebop2");

            showDialog(ProjectsFlyplansActivity.this, "Found Drones", drones, new String[] { "OK", "Abbrechen" },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which==-1)
                                Log.d("Neha", "On button click");
                            //Do your functionality here
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
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


}
