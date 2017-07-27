package com.drone.imavis.mvp.ui.tabs.projectsAdd;

import android.annotation.TargetApi;
import android.app.Activity;
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

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.util.GUIUtils;
import com.drone.imavis.mvp.util.OnRevealAnimationListener;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProjectAdd extends AppCompatActivity {

    @BindView(R.id.activity_project_add_container)
    RelativeLayout container;
    @BindView(R.id.projectAdd_fab_close)
    FloatingActionButton fabClose;

    @BindView(R.id.projectAddTitle)
    TextView title;

    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_add);

        // hide app title
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        title.setText(getSupportActionBar().getTitle());

        project = new Project();

        fabClose.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_close)
                .colorRes(R.color.icons)
                .actionBarSize());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupEnterAnimation();
            setupExitAnimation();
        } else {
            initViews();
        }
    }

    @OnClick(R.id.projectAdd_fab_close)
    public void onFabClicked() {
        addProject(project);
    }

    private void addProject(Project project) {
        // TODO
        // 1. presave check. project name not null
        // 2. save over network to db
        // 3. add button change
        // 4. close this action if success
        //      - and update project list (automatic observable)
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
    public void onIvCloseClicked() {
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
