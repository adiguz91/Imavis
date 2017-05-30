package com.drone.imavis.mvp.ui.tabs.projects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.drone.imavis.mvp.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by adigu on 08.05.2017.
 */

public class ProjectsFragment extends BaseFragment implements IProjectsMvpView {

    ProjectSelected projectSelectedCallback;

    public interface ProjectSelected{
        void onSendProject(Project project);
    }

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.tabs.projects.ProjectsFragment.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject ProjectsPresenter projectsPresenter;
    private ProjectListViewAdapter projectsListViewAdapter;
    //@Inject ProjectListViewAdapter projectsListViewAdapter;

    //@BindView(R.id.projectSwipeListView) ListView projectsListView;
    private ListView projectsListView;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            projectSelectedCallback = (ProjectSelected) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement TextClicked");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_projects, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setContentView(R.layout.activity_projects);
        context = this.getContext();
        //ButterKnife.bind(getContext(), view);
        projectsListView = (ListView) view.findViewById(R.id.projectSwipeListView);

        projectsListViewAdapter = new ProjectListViewAdapter(getContext());
        projectsListView.setAdapter(projectsListViewAdapter);
        projectsListViewAdapter.setMode(Attributes.Mode.Single);
        //projectsListView.setLayoutManager(new LinearLayoutManager(this));
        loadListViewEvents();

        projectsPresenter.attachView(this);
        projectsPresenter.loadProjects();

        //if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
        //    //startService(SyncService.getStartIntent(this));
        //}
    }

    private void loadListViewEvents() {

        projectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //((SwipeLayout)(projectsListView.getChildAt(position - projectsListView.getFirstVisiblePosition()))).open(true);
                //goToActivity(context, FlyplansFragment.class, new Bundle());
                //Project project = (Project) parent.getItemAtPosition(position);
                Project project = (Project) projectsListView.getItemAtPosition(position);
                projectSelectedCallback.onSendProject(project);
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
    public void onDestroy() {
        super.onDestroy();
        projectsPresenter.detachView();
    }

    @Override
    public void showProjects(Projects projects) {
        // TODO
        projectsListViewAdapter.setProjects(projects.getProjectList());
        projectsListViewAdapter.notifyDataSetChanged();
        Toast.makeText(context, "projects sync successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProjectsEmpty() {
        // TODO
        List<Project> projectList = new ArrayList<>();
        projectsListViewAdapter.setProjects(projectList);
        projectsListViewAdapter.notifyDataSetChanged();
        Toast.makeText(context, "projects empty", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        // TODO
        showProjectsEmpty();
        /*
        String jsonString = FileUtil.readAssetFile(context, "data/seed/projects.txt");
        Gson gson = new Gson();
        Projects projects = gson.fromJson(jsonString, Projects.class);
        showProjects(projects);
        */
    }
}
