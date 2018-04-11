package com.drone.imavis.mvp.data;

import com.annimon.stream.Stream;
import com.drone.imavis.mvp.data.local.db.DatabaseHelper;
import com.drone.imavis.mvp.data.local.preference.PreferencesHelper;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.ProjectShort;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.data.model.Task;
import com.drone.imavis.mvp.data.remote.webodm.IWebOdmApiEndpoint;
import com.drone.imavis.mvp.data.remote.webodm.model.Authentication;
import com.drone.imavis.mvp.util.FileUtil;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;

/**
 * Created by adigu on 01.05.2017.
 */

@Singleton
public class DataManager {

    private final IWebOdmApiEndpoint webOdmService;
    private final DatabaseHelper databaseHelper;
    private final PreferencesHelper preferencesHelper;
    private final FileUtil fileUtil;

    private Subscription loginSubscription;

    /*
    @Inject
    public DataManager(RibotsService ribotsService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mRibotsService = ribotsService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }
    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }
    */

    @Inject
    public DataManager(IWebOdmApiEndpoint webOdmService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper, FileUtil fileUtil) {
        this.webOdmService = webOdmService;
        this.databaseHelper = databaseHelper;
        this.preferencesHelper = preferencesHelper;
        this.fileUtil = fileUtil;
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    public Observable<List<Project>> syncProjects() {

        return Observable.zip(
                databaseHelper.getAllProjects().subscribeOn(Schedulers.newThread()),
                getRemoteProjects().subscribeOn(Schedulers.newThread()),
                new BiFunction<List<Project>, Projects, List<Project>>() {
                    @Override
                    public List<Project> apply(List<Project> localProjects, Projects remoteProjects) throws Exception {
                        HashMap<Integer, Project> mapLocalProjects = new HashMap<Integer, Project>();
                        for (Project localProject : localProjects)
                            mapLocalProjects.put(localProject.getOnlineId(), localProject);

                        List<Project> returnProjects = new ArrayList<Project>();
                        for(Project remoteProject : remoteProjects.getProjectList()) {
                            if (mapLocalProjects.containsKey(remoteProject.getOnlineId())) {
                                remoteProject.setId(mapLocalProjects.get(remoteProject.getOnlineId()).getId());
                                //if(remoteProject.getCreatedAt().after(mapLocalProjects.get(remoteProject.getOnlineId()).getCreatedAt())) {
                                //    // if remote date is younger update remote to local
                                //    databaseHelper.updateProject(remoteProject);
                                //}
                            } else {
                                // save remote to local db
                                databaseHelper.saveProject(remoteProject).subscribe(); // bug
                            }
                            returnProjects.add(remoteProject);
                        }
                        return returnProjects;
                    }
                });
    }

    public Observable<Projects> getRemoteProjects() {
        // https://stackoverflow.com/questions/29444297/stack-overflow-when-using-retrofit-rxjava-concatwith/29594194#29594194
        return Observable.range(1, Integer.MAX_VALUE)
                .concatMap( pageNr -> {
                    Observable<Projects> data = webOdmService.getProjects(pageNr.intValue());
                    return data;
                })
                .takeUntil( data -> data.getNext() == null)
                .reduce((projectsFirst, projectsSecond) -> {
                    projectsFirst.getProjectList().addAll(projectsSecond.getProjectList());
                    return projectsFirst;
                }).toObservable();
    }

    public Observable<List<FlyPlan>> syncFlyplans(Project project) {
        // run local and remote requests parallel (because of different threads) and
        // combine the result
        return Observable.zip(
            databaseHelper.getFlyplansFromProject(project).subscribeOn(Schedulers.newThread()),
            webOdmService.getTasks(String.valueOf(project.getOnlineId())).subscribeOn(Schedulers.newThread()),
            new BiFunction<List<FlyPlan>, List<Task>, List<FlyPlan>>() {
                @Override
                public List<FlyPlan> apply(List<FlyPlan> flyplans, List<Task> tasks) throws Exception {
                    Stream.of(flyplans).forEach(flyplan -> {
                        if(flyplan.getTaskId() != null)  {
                            Stream.of(tasks).forEach(task -> {
                                if(flyplan.getTaskId().intValue() == task.getId())
                                    flyplan.setTask(task);
                            });
                        }
                    });
                    return flyplans;
                }
            });
    }

    public Observable<Boolean> login(Authentication authentication) {
        return webOdmService.authentication(authentication)
        .doOnNext(token ->
                preferencesHelper.setAuthorizationToken(token.getToken()) )
        .map(token -> true )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
        //.subscribeOn(io.reactivex.schedulers.Schedulers.io());
    }

    public Single<Project> addProject(Project project) {
        ProjectShort projectShort = new ProjectShort(project.getName(), project.getDescription());
        return webOdmService.addProject(projectShort)
                .flatMap( (projectRecaived) -> {
                    return databaseHelper.createProject(projectRecaived).flatMap( (id) -> {
                        //projectRevaived.setId(id);
                        return Single.just(projectRecaived);
                    });
                });
    }

    public Completable updateProject(Project project) {
        ProjectShort projectShort = new ProjectShort(project.getName(), project.getDescription());
        return webOdmService.updateProject(String.valueOf(project.getOnlineId()), projectShort)
                .flatMapCompletable( (projectRevaived) -> {
                    projectRevaived.setId(project.getId());
                    return databaseHelper.updateProject(projectRevaived);
                });
    }

    public Completable deleteProject(Project project) {
        return webOdmService.deleteProject(String.valueOf(project.getOnlineId()))
                .andThen(databaseHelper.deleteProject(project));
    }

    /**
     * Observable chain. Creates a task, adds it to the flyplan and saves it to the database.
     * @param flyplan
     * @return
     */
    public Single<FlyPlan> addFlyplan(FlyPlan flyplan) {
        return databaseHelper.createFlyplan(flyplan).flatMap((id) -> {
            //flyplan.setId(id);
            return Single.just(flyplan);
        });
    }

    /**
     * When the drone flyes to all waypoints of the flyplan, than the task of the 3D Modeling
     * will be created
     * @param flyplan
     * @return
     */
    public Single<FlyPlan> startFlyplanTask(FlyPlan flyplan) {

        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                String extension = FilenameUtils.getExtension(file.getPath());
                if(extension.toLowerCase().equals("jpg"))
                    return true;
                return false;
            }
        };

        List<MultipartBody.Part> imageParts = fileUtil.getFileParts("images", flyplan.getImageFolderUrl(), fileFilter);
        //flyplan.getTask().setImages(imageParts);

        RequestBody taskName = createPartFromString(flyplan.getName());
        //RequestBody taskOptions = createPartFromString("");

        HashMap<String, RequestBody> taskPartMap = new HashMap<>();
        taskPartMap.put("name", taskName);
        //taskPartMap.put("options", taskOptions);

        //flyplan.getProject().setOnlineId(38);

        return webOdmService.addTask(imageParts, String.valueOf(38), taskPartMap)
                .flatMap( (task) -> {
                    flyplan.setTask(task);
                    return databaseHelper.createFlyplan(flyplan).flatMap((id) -> {
                        //flyplan.setId(id);
                        return Single.just(flyplan);
                    });
                });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    public Completable updateFlyplan(FlyPlan flyplan) {

        /*
        if(flyplan == null) {
            flyplan = new FlyPlan();
            flyplan.setName("TestFlyplan");
            return Completable.fromSingle(Single.just(flyplan));
        }
        return null;
        */

        if(flyplan.getTask() == null)
            return databaseHelper.updateFlyplan(flyplan);

        /*
        return webOdmService.updateTask(String.valueOf(flyplan.getProjectId()),
                String.valueOf(flyplan.getTask().getId()),
                flyplan.getTask())
                .flatMapCompletable( (task) -> {
                    flyplan.setTask(task);
                    return databaseHelper.updateFlyplan(flyplan);
                });*/
        return databaseHelper.updateFlyplan(flyplan);
    }

    public Completable deleteFlyplan(FlyPlan flyplan) {
        if(flyplan.getTask() == null)
            return databaseHelper.deleteFlyplan(flyplan);

        return webOdmService.deleteTask(String.valueOf(flyplan.getProjectId()), String.valueOf(flyplan.getTask().getId()))
                .andThen(databaseHelper.deleteFlyplan(flyplan));
    }

    /*
    public Observable<List<Projects>> getRibots() {
        return databaseHelper.getProjects().distinct();
    }
    */
}