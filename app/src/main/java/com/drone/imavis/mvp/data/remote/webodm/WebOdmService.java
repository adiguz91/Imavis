package com.drone.imavis.mvp.data.remote.webodm;

import android.content.Context;

import com.drone.imavis.mvp.data.local.preference.PreferencesHelper;
import com.drone.imavis.mvp.data.remote.webodm.model.Token;
import com.drone.imavis.mvp.di.ApplicationContext;
import com.drone.imavis.mvp.util.constants.classes.CAll;
import com.drone.imavis.mvp.util.constants.classes.CFiles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by adigu on 06.05.2017.
 */

@Singleton
public class WebOdmService {

    private final PreferencesHelper preferencesHelper;

    private IWebOdmApiEndpoint webOdmService;
    private Context context;

    @Inject
    public WebOdmService(@ApplicationContext Context context, PreferencesHelper preferencesHelper) {
        this.context = context;
        this.preferencesHelper = preferencesHelper;
    }

    public IWebOdmApiEndpoint getWebOdmService() {
        if(webOdmService == null)
            webOdmService = CreateService();
        return webOdmService;
    }

    private IWebOdmApiEndpoint CreateService() {

        // LOGGING
        Timber.plant(new Timber.DebugTree());

        // NETWORK
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Cache
        File networkCacheFile = new File(context.getCacheDir(), CFiles.NETWORK_CACHE_NAME);
        networkCacheFile.mkdir();
        Cache networkCache = new Cache(networkCacheFile, CFiles.NETWORK_CACHE_SIZE);

        // Network Client
        OkHttpClient.Builder okHttpClientBuild = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(CAll.NETWORK_TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(CAll.NETWORK_TIMEOUT_WRITE, TimeUnit.SECONDS)
                .connectTimeout(CAll.NETWORK_TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(CreateRequestInterceptor())
                .cache(networkCache);
        OkHttpClient okHttpClient = okHttpClientBuild.build();

        // PICASSO
        Picasso picasso = new Picasso.Builder(this.context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();

        // API CLIENT
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        // ReactiveX Adapter
        //RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.create();
        //RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        // API SERVICE
        Retrofit webOdmService = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(IWebOdmApiEndpoint.ENDPOINT)
                .build();

        return webOdmService.create(IWebOdmApiEndpoint.class);
    }

    private Interceptor CreateRequestInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder; // = original.newBuilder().header("Authorization", authorizationToken);

                List<String> customAnnotations = original.headers().values("@");
                if(customAnnotations.contains("NoAuth"))
                    requestBuilder = original.newBuilder().removeHeader("@").removeHeader("Authorization");
                else
                    requestBuilder = original.newBuilder().addHeader("Authorization", "JWT " + preferencesHelper.getAuthorizationToken());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }
}
