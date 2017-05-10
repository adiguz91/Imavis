package com.drone.imavis.mvp.data.remote.webodm;

import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Task;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by adigu on 06.05.2017.
 */

public interface IWebOdmApiEndpoint {

    String ENDPOINT = "http://192.168.99.100:8000/api/";

    @Headers("@: NoAuth")
    @GET("token-auth")
    Call<String> authentication(@Query("username") String username, @Query("password") String password);

    @GET("projects")
    Observable<List<Project>> getProjects();

    @GET("projects/{id}")
    Observable<Project> getProject(@Path("id") String projectId);

    @GET("projects/{project_pk}/tasks")
    Observable<List<Task>> getTasks(@Path("project_pk") String projectId);

    @GET("projects/{project_pk}/tasks/{id}")
    Observable<Task> getTasks(@Path("project_pk") String projectId, @Path("id") String taskId);

    // and more ...
}

