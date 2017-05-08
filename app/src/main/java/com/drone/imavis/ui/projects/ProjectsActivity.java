package com.drone.imavis.ui.projects;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.drone.imavis.data.remote.webodm.WebOdmService;

import javax.inject.Inject;

/**
 * Created by adigu on 08.05.2017.
 */

public class ProjectsActivity extends AppCompatActivity {

    @Inject
    ProjectsView view;

    @Inject
    ProjectsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        DaggerProjectsComponent.builder()
                .appComponent(WebOdmService.get(this).component())
                .homeModule(new ProjectsModule(this))
                .build().inject(this);
        */

        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
