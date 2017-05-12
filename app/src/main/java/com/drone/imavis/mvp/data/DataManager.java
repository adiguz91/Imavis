package com.drone.imavis.mvp.data;

import android.util.Log;

import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.data.remote.webodm.IWebOdmApiEndpoint;
import com.drone.imavis.mvp.data.remote.webodm.WebOdmService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import rx.functions.Func1;

/**
 * Created by adigu on 01.05.2017.
 */

@Singleton
public class DataManager {

    private final IWebOdmApiEndpoint webOdmService;
    //private final DatabaseHelper mDatabaseHelper;
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
    public DataManager(IWebOdmApiEndpoint webOdmService) {
        this.webOdmService = webOdmService;
    }

    public SingleObserver<Projects> syncProjects() {
        return webOdmService.getProjects();
    }

    /*
    private SingleObserver<Projects> getProjectsObserver() {
        return new SingleObserver<Projects>() {
            @Override
            public void onSubscribe(Disposable d) {
                //Log.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onSuccess(Projects value) {
                //textView.append(" onNext : value : " + value);
                //textView.append(AppConstant.LINE_SEPARATOR);
                //Log.d(TAG, " onNext value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                //textView.append(" onError : " + e.getMessage());
                //textView.append(AppConstant.LINE_SEPARATOR);
                //Log.d(TAG, " onError : " + e.getMessage());
            }
        };
    }
    */

    /*
    public Observable<List<Ribot>> getRibots() {
        return mDatabaseHelper.getRibots().distinct();
    }
    */
}