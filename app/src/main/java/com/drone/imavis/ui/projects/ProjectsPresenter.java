package com.drone.imavis.ui.projects;

import com.drone.imavis.data.model.Project;
import com.drone.imavis.data.model.Projects;
import com.drone.imavis.data.repository.IRepository;
import com.drone.imavis.data.repository.ProjectRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by adigu on 08.05.2017.
 */

public class ProjectsPresenter {

    private IRepository<Project> projectsRepo = null;
    private final ProjectsView view;
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    
    public ProjectsPresenter(ProjectsView view, ProjectRepository projectsRepo) {
        this.view = view;
        this.projectsRepo = projectsRepo;
    }

    public void onCreate() {
        //compositeSubscription.add(observeLookupButton());
        //compositeSubscription.add(loadSavedState());
    }

    public void onDestroy() {
        compositeSubscription.clear();
    }

}
