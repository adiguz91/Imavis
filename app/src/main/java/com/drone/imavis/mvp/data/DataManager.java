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
import com.drone.imavis.mvp.util.RxUtil;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.util.ConnectConsumer;
import io.reactivex.schedulers.Schedulers;
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
    public DataManager(IWebOdmApiEndpoint webOdmService) { // , DatabaseHelper databaseHelper
        this.webOdmService = webOdmService;
        //this.databaseHelper = databaseHelper;
    }

    public Single<Projects> syncProjects() {
        return webOdmService.getProjects();
    }

    public Observable<List<Task>> syncFlyplans(String projectId) { return webOdmService.getTasks(projectId); }

    public Observable<Boolean> login(Authentication authentication) {
        return webOdmService.authentication(authentication)
        .doOnNext(token ->
                ((WebOdmService)webOdmService).setAuthorizationToken(token.getToken()))
        .map(token -> true )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
        //.subscribeOn(io.reactivex.schedulers.Schedulers.io());
    }

    /*
    public Observable<List<Projects>> getRibots() {
        return databaseHelper.getProjects().distinct();
    }
    */
}