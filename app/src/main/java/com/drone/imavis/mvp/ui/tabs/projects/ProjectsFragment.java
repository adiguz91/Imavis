package com.drone.imavis.mvp.ui.tabs.projects;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.ui.base.BaseFragment;
import com.drone.imavis.mvp.ui.tabs.projectAddOrEdit.ProjectAction;
import com.drone.imavis.mvp.ui.tabs.projectAddOrEdit.ProjectAddOrEditActivity;
import com.drone.imavis.mvp.util.swipelistview.SwipeActionButtons;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemOnClickListener;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by adigu on 08.05.2017.
 */

public class ProjectsFragment extends BaseFragment implements IProjectsMvpView, SwipeItemOnClickListener<Project> {

    ProjectSelected projectSelectedCallback;

    @Override
    public void onCallback(View view, SwipeActionButtons action, int position, Project item) {
        switch (action) {
            case Delete:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                projectsPresenter.deleteProject(item);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Wollen Sie das Project wirklich löschen?").setPositiveButton("Ja", dialogClickListener)
                        .setNegativeButton("Nein", dialogClickListener).show();
                break;
            case Edit:
                // TODO start simple transition for edit project
                startProjectEditActivity(view);
                break;
        }
    }

    public interface ProjectSelected{
        void onSendProject(Project project);
    }

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.tabs.projects.ProjectsFragment.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject ProjectsPresenter projectsPresenter;
    private ProjectSwipeListViewAdaper projectsListViewAdapter;

    private ListView projectsListView;
    private Context context;
    private Activity activity;

    @BindView(R.id.fabProjects)
    FloatingActionButton fabProjects;

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
        View view = inflater.inflate(R.layout.activity_projects, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setContentView(R.layout.activity_projects);
        context = this.getContext();
        activity = this.getActivity();

        fabProjects.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_plus)
                .colorRes(R.color.icons)
                .actionBarSize());

        projectsListView = (ListView) view.findViewById(R.id.projectSwipeListView);
        projectsListViewAdapter = new ProjectSwipeListViewAdaper(getContext(), this);
        projectsListView.setAdapter(projectsListViewAdapter);
        projectsListViewAdapter.setMode(Attributes.Mode.Single);
        loadListViewEvents();

        projectsPresenter.attachView(this);
        projectsPresenter.loadProjects();

        //if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
        //    //startService(SyncService.getStartIntent(this));
        //}
    }

    @OnClick(R.id.fabProjects)
    public void onFabClicked() {
        startProjectActivity(fabProjects, ProjectAction.Add, fabProjects.getTransitionName());
    }

    private void startProjectActivity(View view, ProjectAction projectAction, String transitionName) {
        int requestCode = 1;

        switch (projectAction) {
            case Add:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(activity, fabProjects, fabProjects.getTransitionName());
                    startActivityForResult(new Intent(activity, ProjectAddOrEditActivity.class), requestCode, options.toBundle());
                } else {
                    startActivityForResult(new Intent(activity, ProjectAddOrEditActivity.class), requestCode);
                }
                break;
            case Edit:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(activity, view, transitionName);
                    startActivityForResult(new Intent(activity, ProjectAddOrEditActivity.class), requestCode, options.toBundle());
                } else {
                    startActivityForResult(new Intent(activity, ProjectAddOrEditActivity.class), requestCode);
                }
                break;
        }

    }

    private void startProjectEditActivity(View view) {
        int requestCode = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(activity, view, fabProjects.getTransitionName());
            startActivityForResult(new Intent(activity, ProjectAddOrEditActivity.class), requestCode, options.toBundle());
        } else {
            startActivityForResult(new Intent(activity, ProjectAddOrEditActivity.class), requestCode);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                Project project = data.getParcelableExtra("project"); // get json project added
                projectsListViewAdapter.addItem(project);
            }
        }
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
        /*
        projectsListView.setOnTouchListener ...
        projectsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() ...
        projectsListView.setOnScrollListener(new AbsListView.OnScrollListener() ...
        projectsListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() ...
        */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        projectsPresenter.detachView();
    }

    @Override
    public void showProjects(Projects projects) {
        // TODO
        projectsListViewAdapter.setItems(projects.getProjectList());
        projectsListViewAdapter.notifyDataSetChanged();
        Toast.makeText(context, "projects sync successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProjectsEmpty() {
        // TODO
        List<Project> projectList = new ArrayList<>();
        projectsListViewAdapter.setItems(projectList);
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

    @Override
    public void onDeleteSuccess(Project project) {
        projectsListViewAdapter.deleteItem(project);
    }

    @Override
    public void onDeleteFailed() {

    }
}
