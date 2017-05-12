package com.drone.imavis.mvp.ui.projects;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.di.ConfigPersistent;
import com.drone.imavis.mvp.ui.base.BasePresenter;
import com.drone.imavis.mvp.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rx.Subscription;
import rx.schedulers.Schedulers;

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

    public void loadRibots() {
        checkViewAttached();
        //RxUtil.unsubscribe(mSubscription);
        Projects projects = (Projects) mDataManager.syncProjects();
        if (projects == null) {
            getMvpView().showProjectsEmpty();
        } else {
            getMvpView().showProjects(projects);
        }
    }

}
