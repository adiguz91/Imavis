package com.drone.imavis.mvp.ui.flyplanner;

import android.util.Log;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.di.ConfigPersistent;
import com.drone.imavis.mvp.ui.base.BasePresenter;
import com.drone.imavis.mvp.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import rx.Subscription;

/**
 * Created by adigu on 08.05.2017.
 */

@ConfigPersistent
public class FlyplannerPresenter extends BasePresenter<IFlyplannerActivity> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public FlyplannerPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(IFlyplannerActivity mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void saveFlyplan(FlyPlan flyplan) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);

        flyplan.setNodesJson(flyplan.getPoints().toSimpleNodesJson());
        dataManager.updateFlyplan(flyplan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(new CompletableObserver() {
                   @Override
                   public void onSubscribe(@NonNull Disposable d) {}
                   @Override
                   public void onComplete() {
                       getMvpView().onSaveFlyplanSuccess(flyplan);
                   }
                   @Override
                   public void onError(@NonNull Throwable e) {
                       Log.w("ERROR-PRESENTER", "saveFlyplan: " + e);
                       getMvpView().onSaveFlyplanFailed();

                   }
               }
            );
    }

    public void startFlyplanTask(FlyPlan flyplan) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        dataManager.startFlyplanTask(flyplan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(new SingleObserver<FlyPlan>() {
                   @Override
                   public void onSubscribe(@NonNull Disposable d) {}
                   @Override
                   public void onSuccess(@NonNull FlyPlan flyplan) {
                       getMvpView().onStartFlyplanTaskSuccess(flyplan);
                   }
                   @Override
                   public void onError(@NonNull Throwable e) {
                       getMvpView().onStartFlyplanTaskFailed();
                   }
               }
            );
    }
}
