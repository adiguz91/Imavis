package com.drone.imavis.mvp.ui.projects;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.data.remote.webodm.model.Authentication;
import com.drone.imavis.mvp.data.remote.webodm.model.Token;
import com.drone.imavis.mvp.di.ConfigPersistent;
import com.drone.imavis.mvp.ui.base.BasePresenter;
import com.drone.imavis.mvp.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by adigu on 08.05.2017.
 */

@ConfigPersistent
public class ProjectsPresenter extends BasePresenter<IProjectsMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public ProjectsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(IProjectsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadProjects() {

        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);

        mDataManager.syncProjects()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(new SingleObserver<Projects>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onSuccess(@NonNull Projects projects) {
                if (projects == null) {
                    getMvpView().showProjectsEmpty();
                } else {
                    getMvpView().showProjects(projects);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getMvpView().showError();
            }
        });

        /*
        mSubscription = mDataManager.syncProjects()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(new Subscriber<Projects>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(Projects projects) {
                        if (projects == null) {
                            getMvpView().showProjectsEmpty();
                        } else {
                            getMvpView().showProjects(projects);
                        }
                    }
                });*/
    }

}
