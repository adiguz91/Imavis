package com.drone.imavis.mvp.ui.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.main.MainActivity;
import com.drone.imavis.mvp.ui.tabs.flyplans.FlyplansFragment;
import com.drone.imavis.mvp.ui.tabs.projects.ProjectsFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

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
}
