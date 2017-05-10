package com.drone.imavis.mvp.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.SyncService;
import com.drone.imavis.mvp.ui.MainActivity;
import com.drone.imavis.mvp.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by adigu on 08.05.2017.
 */

public class ProjectsActivity  extends BaseActivity implements ProjectsMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.main.ProjectsActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject ProjectsPresenter projectsPresenter;
    //@Inject RibotsAdapter mRibotsAdapter;

    //@BindView(R.id.recycler_view) RecyclerView mRecyclerView;

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

        //mRecyclerView.setAdapter(mRibotsAdapter);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectsPresenter.attachView(this);
        //projectsPresenter.loadRibots();

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        projectsPresenter.detachView();
    }
}
