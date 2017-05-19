package com.drone.imavis.mvp.ui.flyplans;

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
import com.drone.imavis.mvp.data.model.Flyplan;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.main.MainActivity;
import com.drone.imavis.mvp.ui.projects.ProjectListViewAdapter;
import com.drone.imavis.mvp.ui.projects.ProjectsPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by adigu on 08.05.2017.
 */

public class FlyplansActivity extends BaseActivity implements IFlyplansMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.flyplans.FlyplansActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject
    FlyplansPresenter flyplansPresenter;
    private FlyplanListViewAdapter flyplanListViewAdapter;
    //@Inject FlyplanListViewAdapter flyplanListViewAdapter;

    private int projectId;

    @BindView(R.id.projectSwipeListView) ListView flyplansListView;

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

        flyplanListViewAdapter = new FlyplanListViewAdapter(this);
        flyplansListView.setAdapter(flyplanListViewAdapter);
        flyplanListViewAdapter.setMode(Attributes.Mode.Single);
        //projectsListView.setLayoutManager(new LinearLayoutManager(this));
        loadListViewEvents();

        // getParameter PROJECT_ID from other activity
        projectId = 1;

        flyplansPresenter.attachView(this);
        flyplansPresenter.loadFlyplans(projectId);

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }
    }

    private void loadListViewEvents() {

        flyplansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //((SwipeLayout)(flyplansListView.getChildAt(position - flyplansListView.getFirstVisiblePosition()))).open(true);
                //goToActivity(context, FlyplansActivity.class, new Bundle());
            }
        });
        flyplansListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });
        flyplansListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        flyplansListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });
        flyplansListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        flyplansPresenter.detachView();
    }

    @Override
    public void showFlyplans(List<Flyplan> flyplanList) {
        // TODO
        flyplanListViewAdapter.setFlyplans(flyplanList);
        flyplanListViewAdapter.notifyDataSetChanged();
        Toast.makeText(this, "projects sync successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showFlyplansEmpty() {
        // TODO
        List<Flyplan> flyplanList = new ArrayList<>();
        flyplanListViewAdapter.setFlyplans(flyplanList);
        flyplanListViewAdapter.notifyDataSetChanged();
        Toast.makeText(this, "projects empty", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        // TODO
        showFlyplansEmpty();
    }
}
