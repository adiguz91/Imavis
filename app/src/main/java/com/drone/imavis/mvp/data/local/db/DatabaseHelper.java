package com.drone.imavis.mvp.data.local.db;

import com.drone.imavis.mvp.data.model.DaoMaster;
import com.drone.imavis.mvp.data.model.DaoSession;
import com.drone.imavis.mvp.data.model.FlyPlan;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by adigu on 10.08.2017.
 */

@Singleton
public class DatabaseHelper implements IDatabaseHelper {

    private final DaoSession mDaoSession;

    @Inject
    public DatabaseHelper(DatabaseOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public Observable<Long> createFlyPlan(final FlyPlan flyPlan) {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mDaoSession.getFlyPlanDao().insert(flyPlan);
            }
        });
    }

    @Override
    public Observable<Boolean> deleteFlyPlan(FlyPlan flyPlan) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getFlyPlanDao().deleteByKey(flyPlan.getId());
                return true;
            }
        });
    }

    @Override
    public Observable<List<FlyPlan>> getAllFlyPlans() {
        return Observable.fromCallable(new Callable<List<FlyPlan>>() {
            @Override
            public List<FlyPlan> call() throws Exception {
                return mDaoSession.getFlyPlanDao().loadAll();
            }
        });
    }

    @Override
    public Observable<FlyPlan> getFlyPlan(Long id) {
        return Observable.fromCallable(new Callable<FlyPlan>() {
            @Override
            public FlyPlan call() throws Exception {
                return mDaoSession.getFlyPlanDao().load(id);
            }
        });
    }

    @Override
    public Observable<Boolean> updateFlyPlan(FlyPlan flyPlan) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getFlyPlanDao().update(flyPlan);
                return true;
            }
        });
    }

    /*
    @Override
    public Observable<Boolean> isQuestionEmpty() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !(mDaoSession.getQuestionDao().count() > 0);
            }
        });
    }
    */
}
