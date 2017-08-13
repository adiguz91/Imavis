package com.drone.imavis.mvp.ui.tabs.flyplanAddOrEdit;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.ProjectShort;
import com.drone.imavis.mvp.di.ConfigPersistent;
import com.drone.imavis.mvp.ui.base.BasePresenter;
import com.drone.imavis.mvp.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import rx.Subscription;

/**
 * Created by adigu on 08.05.2017.
 */

@ConfigPersistent
public class FlyplanAddOrEditPresenter extends BasePresenter<IFlyplanAddOrEditMvpView> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public FlyplanAddOrEditPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(IFlyplanAddOrEditMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void addFlyplan(FlyPlan flyplan) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        dataManager.addFlyplan(flyplan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(new SingleObserver<FlyPlan>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull FlyPlan flyPlan) {
                        getMvpView().onAddSuccess(flyplan);
                    }

                   @Override
                   public void onError(@NonNull Throwable e) {
                       getMvpView().onAddFailed();
                   }
               }
            );
    }

    public void editFlyplan(FlyPlan flyplan) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        dataManager.updateFlyplan(flyplan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(new CompletableObserver() {
                   @Override
                   public void onSubscribe(@NonNull Disposable d) {}

                   @Override
                   public void onComplete() {
                       getMvpView().onEditSuccess(flyplan);
                   }

                   @Override
                   public void onError(@NonNull Throwable e) {
                       getMvpView().onEditFailed();
                   }
               }
            );
    }
}
