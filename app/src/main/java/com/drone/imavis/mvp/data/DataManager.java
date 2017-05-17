package com.drone.imavis.mvp.data;

import android.util.Log;

import com.drone.imavis.mvp.data.local.DatabaseHelper;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.data.model.Task;
import com.drone.imavis.mvp.data.remote.webodm.IWebOdmApiEndpoint;
import com.drone.imavis.mvp.data.remote.webodm.WebOdmService;
import com.drone.imavis.mvp.data.remote.webodm.model.Authentication;
import com.drone.imavis.mvp.data.remote.webodm.model.Token;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by adigu on 01.05.2017.
 */

@Singleton
public class DataManager {

    private final IWebOdmApiEndpoint webOdmService;
    //private final DatabaseHelper databaseHelper;
    //private final PreferencesHelper mPreferencesHelper;

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
    public DataManager(IWebOdmApiEndpoint webOdmService) { // , DatabaseHelper databaseHelper
        this.webOdmService = webOdmService;
        //this.databaseHelper = databaseHelper;
    }

    public Single<Projects> syncProjects() {
        return webOdmService.getProjects();
    }

    public Observable<List<Task>> syncFlyplans(String projectId) { return webOdmService.getTasks(projectId); }

    public Single<Token> authorize(Authentication authentication) {
        return webOdmService.authentication(authentication);
    }

    /*
    public Observable<List<Projects>> getRibots() {
        return databaseHelper.getProjects().distinct();
    }
    */
}