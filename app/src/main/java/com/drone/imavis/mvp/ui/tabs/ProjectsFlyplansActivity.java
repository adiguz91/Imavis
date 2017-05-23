package com.drone.imavis.mvp.ui.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.ui.DemoFragment;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.main.MainActivity;
import com.drone.imavis.mvp.ui.tabs.flyplans.FlyplansActivity;
import com.drone.imavis.mvp.ui.tabs.projects.IProjectsMvpView;
import com.drone.imavis.mvp.ui.tabs.projects.ProjectListViewAdapter;
import com.drone.imavis.mvp.ui.tabs.projects.ProjectsActivity;
import com.drone.imavis.mvp.ui.tabs.projects.ProjectsPresenter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adigu on 23.05.2017.
 */

public class ProjectsFlyplansActivity extends BaseActivity {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.projects.ProjectsFlyplansActivity.EXTRA_TRIGGER_SYNC_FLAG";

    //@Inject
    //ProjectsPresenter projectsPresenter;
    //private ProjectListViewAdapter projectsListViewAdapter;
    //@Inject ProjectListViewAdapter projectsListViewAdapter;

    //@BindView(R.id.projectSwipeListView)
    //ListView projectsListView;

    private Context context;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
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

        // do stuff

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Projects", DemoFragment.class)
                .add("Flyplans", DemoFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        //projectsPresenter.attachView(this);
        //projectsPresenter.loadProjects();

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //projectsPresenter.detachView();
    }

}
