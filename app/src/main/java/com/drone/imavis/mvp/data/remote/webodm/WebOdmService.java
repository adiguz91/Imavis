package com.drone.imavis.mvp.data.remote.webodm;

import android.content.Context;

import com.drone.imavis.mvp.data.remote.webodm.model.Authentication;
import com.drone.imavis.mvp.data.remote.webodm.model.Token;
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

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by adigu on 06.05.2017.
 */

public class WebOdmService {

    private IWebOdmApiEndpoint webOdmService;
    private Context context;
    private String authorizationToken;

    public WebOdmService(Context context, String authorizationToken) {
        this.context = context;
        this.authorizationToken = authorizationToken;
    }

    public IWebOdmApiEndpoint getWebOdmService() {
        if(webOdmService == null)
            webOdmService = CreateService();
        return webOdmService;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    private IWebOdmApiEndpoint CreateService() {

        // Logging
        Timber.plant(new Timber.DebugTree());

        // NETWORK
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        File networkCacheFile = new File(context.getCacheDir(), CFiles.NETWORK_CACHE_NAME);
        networkCacheFile.mkdir();
        Cache networkCache = new Cache(networkCacheFile, CFiles.NETWORK_CACHE_SIZE);

        OkHttpClient.Builder okHttpClientBuild = new OkHttpClient.Builder()
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

        //RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.create();
        //RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit webOdmService = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(IWebOdmApiEndpoint.ENDPOINT)
                .build();

        this.webOdmService = webOdmService.create(IWebOdmApiEndpoint.class);
        getAuthenticationToken(new Authentication("admin", "admin"));
        return this.webOdmService;
    }

    public void getAuthenticationToken(Authentication userCredentials) {
        this.webOdmService.authentication(userCredentials)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .subscribe(new SingleObserver<Token>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onSuccess(@NonNull Token token) {
                authorizationToken = token.getToken();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                authorizationToken = null;
            }
        });
    }

    private Interceptor CreateRequestInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder().header("Authorization", authorizationToken);

                // http://stackoverflow.com/questions/37757520/retrofit-2-elegant-way-of-adding-headers-in-the-api-level
                List<String> customAnnotations = original.headers().values("@");
                if(customAnnotations.contains("NoAuth")) {
                    requestBuilder = original.newBuilder().removeHeader("@").removeHeader("Authorization");
                    //requestBuilder = original.newBuilder().removeHeader("Authorization");
                }
                else {
                    requestBuilder = original.newBuilder().addHeader("Authorization", "JWT " + authorizationToken);
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }
}
