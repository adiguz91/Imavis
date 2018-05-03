package com.drone.imavis.mvp.data.remote.webodm;

import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.ProjectShort;
import com.drone.imavis.mvp.data.model.Projects;
import com.drone.imavis.mvp.data.model.Task;
import com.drone.imavis.mvp.data.remote.webodm.model.Authentication;
import com.drone.imavis.mvp.data.remote.webodm.model.Token;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by adigu on 06.05.2017.
 */

public interface IWebOdmApiEndpoint {

    String ENDPOINT = "http://10.0.0.9:8000/api/";

    @Headers("@: NoAuth")
    @POST("token-auth/")
    Observable<Token> authentication(@Body Authentication authentication);

    /* PROJECT */

    @GET("projects/")
    Observable<Projects> getProjects(@Query("page") int page);

    @GET("projects/{id}")
    Observable<Project> getProject(@Path("id") String id);

    @POST("projects/")
        // POST requests needs the ending slash!
    Single<Project> addProject(@Body ProjectShort project);

    @PATCH("projects/{id}/")
    Single<Project> updateProject(@Path("id") String id, @Body ProjectShort project);

    @DELETE("projects/{id}/")
        // DELETE requests needs the ending slash!
    Completable deleteProject(@Path("id") String id);

    /* TASK */

    @GET("projects/{projectId}/tasks")
    Observable<List<Task>> getTasks(@Path("projectId") String projectId);

    @GET("projects/{projectId}/tasks/{taskId}")
    Observable<Task> getTask(@Path("projectId") String projectId, @Path("taskId") String taskId);

    //@Headers({"Accept: application/json",})
    @Multipart
    @POST("projects/{projectId}/tasks/")
    // POST requests needs the ending slash!, or MultipartBody.Part[] images
    //Single<Task> addTask(@Part List<MultipartBody.Part> images, @Path("projectId") String projectId, @Body Task task);
    Single<Task> addTask(@Part List<MultipartBody.Part> images, @Path("projectId") String projectId, @PartMap() Map<String, RequestBody> taskPartMap);

    @PATCH("projects/{projectId}/tasks/{taskId}/")
    Single<Task> updateTask(@Path("projectId") String projectId, @Path("taskId") String taskId, @Body Task task);

    @DELETE("projects/{projectId}/tasks/{taskId}/")
        // DELETE requests needs the ending slash!
    Completable deleteTask(@Path("projectId") String projectId, @Path("taskId") String taskId);

    // and more ...
}

