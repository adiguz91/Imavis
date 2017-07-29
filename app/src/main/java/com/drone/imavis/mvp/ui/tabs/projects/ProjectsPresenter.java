package com.drone.imavis.mvp.ui.tabs.projects;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.di.ConfigPersistent;
import com.drone.imavis.mvp.ui.base.BasePresenter;
import com.drone.imavis.mvp.util.FileUtil;
import com.drone.imavis.mvp.util.RxUtil;
import com.google.gson.Gson;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.CompletableSubject;
import rx.CompletableSubscriber;
import rx.Subscription;

/**
 * Created by adigu on 08.05.2017.
 */

@ConfigPersistent
public class ProjectsPresenter extends BasePresenter<IProjectsMvpView> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public ProjectsPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(IProjectsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void deleteProject(Project project) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        dataManager.deleteProject(project.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        getMvpView().onDeleteSuccess(project);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getMvpView().onDeleteFailed();
                    }
                });
    }

    public void loadProjects() {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        dataManager.syncProjects()
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
    }
}
