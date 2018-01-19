package com.drone.imavis.mvp.ui.searchwlan;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.di.ConfigPersistent;
import com.drone.imavis.mvp.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by adigu on 08.05.2017.
 */

@ConfigPersistent
public class SearchWlanPresenter extends BasePresenter<ISearchWlan> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public SearchWlanPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(ISearchWlan mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

}
