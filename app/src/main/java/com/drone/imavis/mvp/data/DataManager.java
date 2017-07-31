package com.drone.imavis.mvp.data;

import com.drone.imavis.mvp.data.local.preference.PreferencesHelper;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.ProjectShort;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.data.model.Task;
import com.drone.imavis.mvp.data.remote.webodm.IWebOdmApiEndpoint;
import com.drone.imavis.mvp.data.remote.webodm.model.Authentication;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import rx.Subscription;

/**
 * Created by adigu on 01.05.2017.
 */

@Singleton
public class DataManager {

    private final IWebOdmApiEndpoint webOdmService;
    //private final DatabaseHelper databaseHelper;
    private final PreferencesHelper preferencesHelper;

    private Subscription loginSubscription;

    /*
    @Inject
    public DataManager(RibotsService ribotsService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mRibotsService = ribotsService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }
    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }
    */

    @Inject
    public DataManager(IWebOdmApiEndpoint webOdmService, PreferencesHelper preferencesHelper) { // , DatabaseHelper databaseHelper
        this.webOdmService = webOdmService;
        //this.databaseHelper = databaseHelper;
        this.preferencesHelper = preferencesHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    public Single<Projects> syncProjects() {
        return webOdmService.getProjects();
    }

    public Observable<List<Task>> syncFlyplans(String projectId) {
        return webOdmService.getTasks(projectId);
    }

    public Observable<Boolean> login(Authentication authentication) {
        return webOdmService.authentication(authentication)
        .doOnNext(token ->
                preferencesHelper.setAuthorizationToken(token.getToken()) )
        .map(token -> true )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
        //.subscribeOn(io.reactivex.schedulers.Schedulers.io());
    }

    public Single<Project> addProject(ProjectShort project) {
        return webOdmService.addProject(project);
    }

    public Single<Project> updateProject(String id, ProjectShort project) {
        return webOdmService.updateProject(id, project);
    }

    public Completable deleteProject(int id) {
        return webOdmService.deleteProject(Integer.toString(id));
    }

    public Completable deleteFlyplan(int projectId, int taskId) {
        return webOdmService.deleteTask(Integer.toString(projectId), Integer.toString(taskId));
    }

    /*
    public Observable<List<Projects>> getRibots() {
        return databaseHelper.getProjects().distinct();
    }
    */
}