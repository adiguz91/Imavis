package com.drone.imavis.mvp.ui.tabs.flyplans;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.model.Flyplan;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Task;
import com.drone.imavis.mvp.di.ConfigPersistent;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.FlyPlan;
import com.drone.imavis.mvp.ui.base.BasePresenter;
import com.drone.imavis.mvp.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import rx.Subscription;

/**
 * Created by adigu on 08.05.2017.
 */

@ConfigPersistent
public class FlyplansPresenter extends BasePresenter<IFlyplansMvpView> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public FlyplansPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(IFlyplansMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadFlyplans(int projectId) {
        if(projectId < 0) // show empty list
            return;

        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        dataManager.syncFlyplans(String.valueOf(projectId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                //.retry() // authorization token is not finished
                .subscribe(new Observer<List<Task>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onNext(@NonNull List<Task> taskList) {
                        if (taskList == null || taskList.size() == 0) {
                            getMvpView().showFlyplansEmpty();
                        } else {
                            // transformation wrapper
                            List<FlyPlan> flyplanList = new ArrayList<FlyPlan>();
                            for(Task task : taskList) {
                                FlyPlan flyplan = new FlyPlan(task);
                                flyplanList.add(flyplan);
                            }
                            getMvpView().showFlyplans(flyplanList);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getMvpView().showError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteFlyplan(FlyPlan flyplan) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        dataManager.deleteFlyplan(flyplan.getTask().getId(), flyplan.getTask().getProject())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        getMvpView().onDeleteSuccess(flyplan);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getMvpView().onDeleteFailed();
                    }
                });
    }

}
