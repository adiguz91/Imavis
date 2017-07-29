package com.drone.imavis.mvp.data.remote.webodm;

import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.ProjectShort;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.data.model.Task;
import com.drone.imavis.mvp.data.remote.webodm.model.Authentication;
import com.drone.imavis.mvp.data.remote.webodm.model.Token;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by adigu on 06.05.2017.
 */

public interface IWebOdmApiEndpoint {

    String ENDPOINT = "http://192.168.99.100:8000/api/";

    @Headers("@: NoAuth")
    @POST("token-auth/")
    Observable<Token> authentication(@Body Authentication authentication);

    /* PROJECT */

    @GET("projects")
    Single<Projects> getProjects();

    @GET("projects/{id}")
    Observable<Project> getProject(@Path("id") String id);

    @POST("projects/") // POST requests needs the ending slash!
    Single<Project> addProject(@Body ProjectShort project);

    @PATCH("projects/{id}")
    Single<Project> updateProject(@Path("id") String id, @Body Project project);

    @DELETE("projects/{id}/") // DELETE requests needs the ending slash!
    Completable deleteProject(@Path("id") String id);

    /* TASK */

    @GET("projects/{project_pk}/tasks")
    Observable<List<Task>> getTasks(@Path("project_pk") String projectId);

    @GET("projects/{project_pk}/tasks/{id}")
    Observable<Task> getTask(@Path("project_pk") String projectId, @Path("id") String taskId);

    // and more ...
}

