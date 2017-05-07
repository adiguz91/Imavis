package com.drone.imavis.data.remote.webodm;

import android.content.Context;

import com.drone.imavis.util.constants.classes.CFiles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
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

    public WebOdmService(Context context) {
        this.context = context;
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

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(networkCache)
                .build();

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
