package com.drone.imavis.mvp.ui.tabs.projectsAdd;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.ProjectShort;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.tabs.ProjectsFlyplansActivity;
import com.drone.imavis.mvp.ui.tabs.projects.ProjectsPresenter;
import com.drone.imavis.mvp.util.GUIUtils;
import com.drone.imavis.mvp.util.OnRevealAnimationListener;
import com.drone.imavis.mvp.util.StringUtil;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProjectAddActivity extends BaseActivity implements IProjectAddMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.projects.ProjectsActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject
    ProjectAddPresenter projectAddPresenter;

    @BindView(R.id.activity_project_add_container)
    RelativeLayout container;
    @BindView(R.id.projectAdd_fab_close)
    FloatingActionButton fabClose;

    @BindView(R.id.projectAddTitle)
    TextView title;

    @BindView(R.id.projectsAddEditTextName)
    MaterialEditText projectName;

    @BindView(R.id.projectsAddEditTextDescription)
    MaterialEditText projectDescription;

    private Context context;

    @BindView(R.id.buttonProjectAdd) ActionProcessButton buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_projects_add);

        // hide app title
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        context = this;

        title.setText(getSupportActionBar().getTitle());
        buttonAdd.setProgress(0);
        buttonAdd.setMode(ActionProcessButton.Mode.ENDLESS);
        buttonAdd.setOnClickListener(onClick -> addProject() );

        fabClose.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_close)
                .colorRes(R.color.icons)
                .actionBarSize());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupEnterAnimation();
            setupExitAnimation();
        } else {
            initViews();
        }

        projectAddPresenter.attachView(this);
    }

    @OnClick(R.id.projectAdd_fab_close)
    public void onFabClicked() {
        onCloseClicked();
    }

    public void addProject() {
        if(projectName.validate()) {
            ProjectShort project = new ProjectShort();
            project.setName(projectName.getEditableText().toString());
            project.setDescription(projectDescription.getEditableText().toString());

            buttonAdd.setProgress(1);
            buttonAdd.setEnabled(false);
            projectName.setEnabled(false);
            projectDescription.setEnabled(false);

            projectAddPresenter.addProject(project);
        }
    }

    @Override
    public void onAddSuccess(Project project) {
        buttonAdd.setProgress(100); // 100 : Success
        onComplete();
        passDataBack(project);
        onCloseClicked();
    }

    private void passDataBack(Project project) {
        Intent intent = new Intent();
        intent.putExtra("project", project);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onAddFailed() {
        buttonAdd.setProgress(-1); // -1 : Failed
        onComplete();
    }

    @Override
    public void onComplete() {
        // after button click timeout is reached!
        buttonAdd.setEnabled(true);
        projectName.setEnabled(true);
        projectDescription.setEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.changebounds_with_arcmotion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {}
            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow(container);
            }
            @Override
            public void onTransitionCancel(Transition transition) {}
            @Override
            public void onTransitionPause(Transition transition) {}
            @Override
            public void onTransitionResume(Transition transition) {}
        });
    }

    private void animateRevealShow(final View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        GUIUtils.animateRevealShow(this, container, fabClose.getWidth() / 2, R.color.colorPrimary,
            cx, cy, new OnRevealAnimationListener() {
                @Override
                public void onRevealHide() {}
                @Override
                public void onRevealShow() {
                    initViews();
                }
            });
    }

    private void initViews() {
        new Handler(Looper.getMainLooper()).post(() -> {
            Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            animation.setDuration(300);
            //mLlContainer.startAnimation(animation);
            //mIvClose.startAnimation(animation);
            //mLlContainer.setVisibility(View.VISIBLE);
            //mIvClose.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onBackPressed() {
        GUIUtils.animateRevealHide(this, container, R.color.colorPrimary, fabClose.getWidth() / 2,
                new OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {
                        backPressed();
                    }
                    @Override
                    public void onRevealShow() {}
                });
    }

    @OnClick(R.id.projectAdd_fab_close)
    public void onCloseClicked() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            onBackPressed();
        } else {
            backPressed();
        }
    }

    private void backPressed() {
        super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupExitAnimation() {
        Fade fade = new Fade();
        getWindow().setReturnTransition(fade);
        fade.setDuration(getResources().getInteger(R.integer.animation_duration));
    }
}
