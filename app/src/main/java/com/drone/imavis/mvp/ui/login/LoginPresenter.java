package com.drone.imavis.mvp.ui.login;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.data.remote.webodm.WebOdmService;
import com.drone.imavis.mvp.data.remote.webodm.model.Authentication;
import com.drone.imavis.mvp.data.remote.webodm.model.Token;
import com.drone.imavis.mvp.di.ConfigPersistent;
import com.drone.imavis.mvp.ui.base.BasePresenter;
import com.drone.imavis.mvp.ui.login.ILoginMvpView;
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
public class LoginPresenter extends BasePresenter<ILoginMvpView> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(ILoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void login() {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);

        Authentication authentication = new Authentication("admin", "admin");
        dataManager.login(authentication)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}
            @Override
            public void onNext(@NonNull Boolean isLoggedIn) {
                getMvpView().onLoginSuccess();
            }
            @Override
            public void onError(@NonNull Throwable e) {
                // todo
                if(getMvpView() != null)
                    getMvpView().onLoginFailed();
            }
            @Override
            public void onComplete() {}
        });
    }
}
