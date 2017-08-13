package com.drone.imavis.mvp.ui.tabs.projectAddOrEdit;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.ProjectShort;
import com.drone.imavis.mvp.di.ConfigPersistent;
import com.drone.imavis.mvp.ui.base.BasePresenter;
import com.drone.imavis.mvp.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import rx.Subscription;

/**
 * Created by adigu on 08.05.2017.
 */

@ConfigPersistent
public class ProjectAddOrEditPresenter extends BasePresenter<IProjectAddOrEditMvpView> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public ProjectAddOrEditPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(IProjectAddOrEditMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void addProject(Project project) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        dataManager.addProject(project)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(new SingleObserver<Project>() {
                   @Override
                   public void onSubscribe(@NonNull Disposable d) {}

                   @Override
                   public void onSuccess(@NonNull Project project) {
                       getMvpView().onAddSuccess(project);
                   }

                   @Override
                   public void onError(@NonNull Throwable e) {
                       getMvpView().onAddFailed();
                   }
               }
            );
    }

    public void editProject(Project project) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        dataManager.updateProject(project)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(new SingleObserver<Project>() {
                   @Override
                   public void onSubscribe(@NonNull Disposable d) {}

                   @Override
                   public void onSuccess(@NonNull Project project) {
                       getMvpView().onEditSuccess(project);
                   }

                   @Override
                   public void onError(@NonNull Throwable e) {
                       getMvpView().onEditFailed();
                   }
               }
            );
    }
}
