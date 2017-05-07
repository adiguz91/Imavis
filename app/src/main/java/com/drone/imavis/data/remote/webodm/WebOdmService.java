package com.drone.imavis.data.remote.webodm;

import android.content.Context;

import com.drone.imavis.data.remote.webodm.model.Authentication;
import com.drone.imavis.util.constants.classes.CFiles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by adigu on 06.05.2017.
 */

public class WebOdmService {

    private WebOdmService webOdmService;
    private Context context;
    private String authorizationToken;

    public WebOdmService(Context context, String authorizationToken) {
        this.context = context;
        this.authorizationToken = authorizationToken;
    }

    public WebOdmService getWebOdmService() {
        if(webOdmService == null)
            webOdmService = CreateService();
        return webOdmService;
    }

    private WebOdmService CreateService() {

        // Logging
        Timber.plant(new Timber.DebugTree());

        // NETWORK
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });

        File networkCacheFile = new File(context.getCacheDir(), CFiles.NETWORK_CACHE_NAME);
        networkCacheFile.mkdir();
        Cache networkCache = new Cache(networkCacheFile, CFiles.NETWORK_CACHE_SIZE);

        OkHttpClient.Builder okHttpClientBuild = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(networkCache);

        okHttpClientBuild.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder().header("Authorization", authorizationToken);

                // http://stackoverflow.com/questions/37757520/retrofit-2-elegant-way-of-adding-headers-in-the-api-level
                List<String> customAnnotations = original.headers().values("@");
                if(customAnnotations.contains("@: NoAuth")) {
                    requestBuilder = original.newBuilder().removeHeader("@").removeHeader("Authorization");
                    //requestBuilder = original.newBuilder().removeHeader("Authorization");
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient okHttpClient = okHttpClientBuild.build();

        // PICASSO
        Picasso picasso = new Picasso.Builder(this.context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();

        // API CLIENT
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit webOdmService = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(IWebOdmApiEndpoint.ENDPOINT)
                .build();

        return webOdmService.create(WebOdmService.class);
    }
}
