package com.drone.imavis.mvp.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.swipe.SwipeLayout;
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
            "com.drone.imavis.mvp.ui.projects.ProjectsActivity.EXTRA_TRIGGER_SYNC_FLAG";

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

    private void loadSwipeLayout() {
        SwipeLayout swipeLayout =  (SwipeLayout)findViewById(R.id.projectSwipeLayout);

        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        projectsPresenter.detachView();
    }
}
