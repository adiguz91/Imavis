package com.drone.imavis.mvp.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.SyncService;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.ui.flyplans.FlyplansActivity;
import com.drone.imavis.mvp.ui.main.MainActivity;
import com.drone.imavis.mvp.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adigu on 08.05.2017.
 */

public class ProjectsActivity extends BaseActivity implements IProjectsMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.projects.ProjectsActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject ProjectsPresenter projectsPresenter;
    private ProjectListViewAdapter projectsListViewAdapter;
    //@Inject ProjectListViewAdapter projectsListViewAdapter;

    @BindView(R.id.projectSwipeListView) ListView projectsListView;

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
        setContentView(R.layout.activity_projects);
        ButterKnife.bind(this);

        projectsListViewAdapter = new ProjectListViewAdapter(this);
        projectsListView.setAdapter(projectsListViewAdapter);
        projectsListViewAdapter.setMode(Attributes.Mode.Single);
        //projectsListView.setLayoutManager(new LinearLayoutManager(this));
        loadListViewEvents();

        projectsPresenter.attachView(this);
        projectsPresenter.loadProjects();

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }
    }

    private void loadListViewEvents() {

        projectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ((SwipeLayout)(projectsListView.getChildAt(position - projectsListView.getFirstVisiblePosition()))).open(true);
            //goToActivity(context, FlyplansActivity.class, new Bundle());
            //projectsPresenter.loadProjects();
            }
        });
        projectsListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            Log.e("ListView", "OnTouch");
            return false;
            }
        });
        projectsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(context, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
            return true;
            }
        });
        projectsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });

        projectsListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        projectsPresenter.detachView();
    }

    @Override
    public void showProjects(Projects projects) {
        // TODO
        projectsListViewAdapter.setProjects(projects.getProjectList());
        projectsListViewAdapter.notifyDataSetChanged();
        Toast.makeText(this, "projects sync successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProjectsEmpty() {
        // TODO
        List<Project> projectList = new ArrayList<>();
        projectsListViewAdapter.setProjects(projectList);
        projectsListViewAdapter.notifyDataSetChanged();
        Toast.makeText(this, "projects empty", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        // TODO
        showProjectsEmpty();
    }
}
