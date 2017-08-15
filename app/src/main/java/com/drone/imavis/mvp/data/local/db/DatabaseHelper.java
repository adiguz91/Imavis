package com.drone.imavis.mvp.data.local.db;

import com.drone.imavis.mvp.data.model.DaoMaster;
import com.drone.imavis.mvp.data.model.DaoSession;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by adigu on 10.08.2017.
 */

@Singleton
public class DatabaseHelper implements IDatabaseHelper {

    private final DaoSession mDaoSession;

    @Inject
    public DatabaseHelper(DatabaseOpenHelper dbOpenHelper) {
        // TODO pref with attribute ENCRYPTED, return getEncryptedWritableDb
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    /* FLYPLAN */

    @Override
    public Observable<List<FlyPlan>> getAllFlyplans() {
        return Observable.fromCallable(new Callable<List<FlyPlan>>() {
            @Override
            public List<FlyPlan> call() throws Exception {
                return mDaoSession.getFlyPlanDao().loadAll();
            }
        });
    }

    @Override
    public Single<FlyPlan> getFlyplan(Long id) {
        return Single.fromCallable(new Callable<FlyPlan>() {
            @Override
            public FlyPlan call() throws Exception {
                return mDaoSession.getFlyPlanDao().load(id);
            }
        });
    }

    @Override
    public Completable updateFlyplan(FlyPlan flyplan) {
        return Completable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getFlyPlanDao().update(flyplan);
                return true;
            }
        });
    }

    @Override
    public Single<Long> createFlyplan(final FlyPlan flyplan) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mDaoSession.getFlyPlanDao().insert(flyplan);
            }
        });
    }

    @Override
    public Completable deleteFlyplan(FlyPlan flyplan) {
        return Completable.fromCallable(new Callable<Object>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getFlyPlanDao().deleteByKey(flyplan.getId());
                return true;
            }
        });
    }

    /* PROJECT */

    @Override
    public Observable<List<Project>> getAllProjects() {
        return Observable.fromCallable(new Callable<List<Project>>() {
            @Override
            public List<Project> call() throws Exception {
                return mDaoSession.getProjectDao().loadAll();
            }
        });
    }

    @Override
    public Single<Project> getProject(Long id) {
        return Single.fromCallable(new Callable<Project>() {
            @Override
            public Project call() throws Exception {
                return mDaoSession.getProjectDao().load(id);
            }
        });
    }

    @Override
    public Completable updateProject(Project project) {
        return Completable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getProjectDao().update(project);
                return true;
            }
        });
    }

    @Override
    public Single<Long> createProject(Project project) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mDaoSession.getProjectDao().insert(project);
            }
        });
    }

    @Override
    public Completable deleteProject(Project project) {
        return Completable.fromCallable(new Callable<Object>() {
            @Override
            public Boolean call() throws Exception {
                if(project.getId() != null)
                    mDaoSession.getProjectDao().deleteByKey(project.getId());
                return true;
            }
        });
    }

    /* OTHER */

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
