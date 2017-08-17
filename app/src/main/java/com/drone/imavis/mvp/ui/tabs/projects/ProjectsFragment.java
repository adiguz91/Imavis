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
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.ui.base.BaseFragment;
import com.drone.imavis.mvp.ui.tabs.projectAddOrEdit.ProjectAction;
import com.drone.imavis.mvp.ui.tabs.projectAddOrEdit.ProjectAddOrEditActivity;
import com.drone.imavis.mvp.util.DialogUtil;
import com.drone.imavis.mvp.util.swipelistview.SwipeActionButtons;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemOnClickListener;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by adigu on 08.05.2017.
 */

public class ProjectsFragment extends BaseFragment implements IProjectsMvpView, SwipeItemOnClickListener<Project> {

    public interface ProjectSelected{
        void onSendProject(Project project);
    }

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.tabs.projects.ProjectsFragment.EXTRA_TRIGGER_SYNC_FLAG";

    private ProjectSelected projectSelectedCallback;
    private int selectedItem = -1;


    @Inject DialogUtil dialogUtil;
    @Inject ProjectsPresenter projectsPresenter;
    private ProjectSwipeListViewAdaper projectsListViewAdapter;

    private ListView projectsListView;
    private Context context;
    private Activity activity;

    @BindView(R.id.fabAddProject)
    FloatingActionButton fabAddProject;

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

    public ViewParent findParentRecursively(View view, int targetId) {
        if (view.getId() == targetId)
            return (ViewParent)view;
        View parent = (View) view.getParent();
        if (parent == null)
            return null;
        return findParentRecursively(parent, targetId);
    }

    @Override
    public void onCallback(View view, SwipeActionButtons action, int position, Project item) {
        selectedItem = position;
        SwipeLayout swipeView = (SwipeLayout) findParentRecursively(view, R.id.projectItemSwipe);

        switch (action) {
            case Delete:
                String title = "Hinweis";
                String message = "Wollen Sie das Project wirklich löschen?";
                Map<Integer,String> buttons =  new HashMap<Integer,String>();
                buttons.put(DialogInterface.BUTTON_POSITIVE, "Ja");
                buttons.put(DialogInterface.BUTTON_NEGATIVE, "Nein");
                dialogUtil.showSimpleDialogMessage(title, message, buttons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        swipeView.close();
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                projectsPresenter.deleteProject(item);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                });
                break;
            case Edit:
                swipeView.close();
                Intent intent = new Intent(context, ProjectAddOrEditActivity.class);
                intent.putExtra("ProjectAction", ProjectAction.Edit);
                intent.putExtra("Project", item);
                startProjectActivity(view, intent, ProjectAction.Edit, fabAddProject.getTransitionName());
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setContentView(R.layout.activity_projects);
        context = this.getContext();
        activity = this.getActivity();

        fabAddProject.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_plus)
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

    @OnClick(R.id.fabAddProject)
    public void onFabClicked() {
        Intent intent = new Intent(context, ProjectAddOrEditActivity.class);
        intent.putExtra("ProjectAction", ProjectAction.Add);
        startProjectActivity(fabAddProject, intent, ProjectAction.Add, fabAddProject.getTransitionName());
    }

    private void startProjectActivity(View view, Intent intent, ProjectAction projectAction, String transitionName) {
        int requestCode = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view, transitionName);
            startActivityForResult(intent, requestCode, options.toBundle());
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                Project project = data.getParcelableExtra("project");
                ProjectAction projectAction = (ProjectAction) data.getSerializableExtra("ProjectAction");
                if(projectAction == ProjectAction.Add)
                    projectsListViewAdapter.addItem(project);
                else if (projectAction == ProjectAction.Edit)
                    projectsListViewAdapter.updateItem(selectedItem, project);
                selectedItem = -1;
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
    public void showProjects(List<Project> projects) {
        projectsListViewAdapter.setItems(projects);
        projectsListViewAdapter.notifyDataSetChanged();
        //Toast.makeText(context, "projects sync successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProjectsEmpty() {
        List<Project> projectList = new ArrayList<>();
        projectsListViewAdapter.setItems(projectList);
        projectsListViewAdapter.notifyDataSetChanged();
        //Toast.makeText(context, "projects empty", Toast.LENGTH_LONG).show();
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
    public void onDeleteFailed() {}

    @Override
    public void onEditSuccess(int position, Project project) {
        projectsListViewAdapter.updateItem(position, project);
    }

    @Override
    public void onEditFailed() {

    }
}
