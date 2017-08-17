package com.drone.imavis.mvp;

import android.app.Application;
import android.content.Context;

import com.drone.imavis.mvp.data.DataManager;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.ProjectShort;
import com.drone.imavis.mvp.di.component.ApplicationComponent;
import com.drone.imavis.mvp.di.component.DaggerApplicationComponent;
import com.drone.imavis.mvp.di.module.ApplicationModule;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by adigu on 10.05.2017.
 */

public class AppStartup extends Application {

    public class TestData {
        private Long id;
        private Date date;

        public TestData(Long id, Date date) {
            this.id = id;
            this.date = date;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    //@Inject
    //DataManager mDataManager;

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();


        //https://stackoverflow.com/questions/31947433/rxjava-sequence-filtering
        /*
        final String[] output = {""};

        DateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);
        //Date date = format.parse(string);
        //System.out.println(date);
        TestData project1 = null;
        TestData project11 = null;
        TestData project22 = null;
        TestData project2 = null;
        TestData project3 = null;
        TestData project33 = null;
        TestData project4 = null;
        try {
            project1 = new TestData(new Long(1), format.parse("2017.01.01"));
            project11 = new TestData(new Long(1), format.parse("2017.01.02")); // !
            project22 = new TestData(new Long(2), format.parse("2017.01.03")); // !
            project2 = new TestData(new Long(2), format.parse("2017.01.01"));
            project3 = new TestData(new Long(3), format.parse("2017.01.06")); // !
            project33 = null;
            project4 = new TestData(new Long(4), format.parse("2017.01.07")); // 1
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        List<TestData> local = new ArrayList<>();
        local.add(project1);
        local.add(project2);
        local.add(project3);

        List<TestData> remote = new ArrayList<>();
        local.add(project11);
        local.add(project22);
        local.add(project33);
        local.add(project4);

        HashMap<Long, TestData> repositoryData = new HashMap<Long, TestData>();

        for (Iterator it = local.iterator(); it.hasNext(); ) {

            if (!it.hasNext()) {
                // Last item...
            }
        }

        //Observable<TestData> local = Observable.just(project1, project2, project3);
        //Observable<TestData> remote = Observable.just(project11, project22, project33, project4);

        //Observable.concat(just1, just2).firstOrError().subscribe(item -> {
        //    output[0] += item.getName() + " ";
        //});

        //local.flatMap(localItem -> remote.filter(remoteItem -> localItem.date.after(remoteItem.date)))
        //     .first(new TestData(new Long(0), new Date()));
        */

        Iconify.with(new FontAwesomeModule());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            //Fabric.with(this, new Crashlytics());
        }
    }

    public static AppStartup get(Context context) {
        return (AppStartup) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
