package com.drone.imavis.mvp.ui.tabs.flyplanAddOrEdit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.ProjectShort;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.base.IMvpView;
import com.drone.imavis.mvp.util.GUIUtils;
import com.drone.imavis.mvp.util.OnRevealAnimationListener;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlyplanAddOrEditActivity extends BaseActivity implements IFlyplanAddOrEditMvpView, IMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.tabs.flyplanAddOrEdit.FlyplanAddOrEditActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject
    FlyplanAddOrEditPresenter flyplanPresenter;

    @BindView(R.id.activity_flyplan_add_container)
    RelativeLayout container;

    @BindView(R.id.flyplanAdd_fab_close)
    FloatingActionButton fabClose;

    @BindView(R.id.flyplanAddTitle)
    TextView title;

    @BindView(R.id.flyplanAddEditTextName)
    MaterialEditText flyplanName;

    @BindView(R.id.buttonFlyplanAction)
    ActionProcessButton buttonFlyplanAction;

    private Context context;
    private FlyplanAction flyplanAction;
    private FlyPlan flyplan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_flyplans_add);
        ButterKnife.bind(this);
        context = this;

        // get padded data
        flyplan = getIntent().getParcelableExtra("Flyplan");
        flyplanAction = (FlyplanAction) getIntent().getSerializableExtra("FlyplanAction");

        // hide app title
        getSupportActionBar().hide();
        title.setText("Flugplan"); // getSupportActionBar().getTitle()

        buttonFlyplanAction.setProgress(0);
        buttonFlyplanAction.setMode(ActionProcessButton.Mode.ENDLESS);
        buttonFlyplanAction.setOnClickListener(onClick -> sendFlyplan(flyplanAction) );

        if (flyplanAction == FlyplanAction.Add) {
            buttonFlyplanAction.setText("Hinzufügen");
        } else if (flyplanAction == FlyplanAction.Edit) {
            buttonFlyplanAction.setText("Speichern");
            flyplanName.setText(flyplan.getName());
        }

        fabClose.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_close)
                .colorRes(R.color.icons)
                .actionBarSize());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupEnterAnimation();
            setupExitAnimation();
        } else {
            initViews();
        }

        flyplanPresenter.attachView(this);
    }

    @OnClick(R.id.flyplanAdd_fab_close)
    public void onFabClicked() {
        onCloseClicked();
    }

    public void sendFlyplan(FlyplanAction flyplanAction) {
        if(flyplanName.validate()) {
            FlyPlan flyplan = new FlyPlan();
            flyplan.setName(flyplanName.getEditableText().toString());

            buttonFlyplanAction.setProgress(1);
            buttonFlyplanAction.setEnabled(false);
            flyplanName.setEnabled(false);

            if(flyplanAction == FlyplanAction.Add)
                flyplanPresenter.addFlyplan(flyplan);
            else if (flyplanAction == FlyplanAction.Edit) {
                flyplanPresenter.editFlyplan(flyplan);
            }
        }
    }

    private void passDataBack(FlyPlan flyplan) {
        Intent intent = new Intent();
        intent.putExtra("Flyplan", flyplan);
        intent.putExtra("FlyplanAction", flyplanAction);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onAddSuccess(FlyPlan flyplan) {
        buttonFlyplanAction.setProgress(100); // 100 : Success
        onComplete();
        this.flyplan = flyplan;
        onCloseClicked();
    }

    @Override
    public void onAddFailed() {
        buttonFlyplanAction.setProgress(-1); // -1 : Failed
        this.flyplan = null;
        onComplete();
    }

    public void onComplete() {
        buttonFlyplanAction.setEnabled(true);
        flyplanName.setEnabled(true);
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
        passDataBack(flyplan);
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

    @OnClick(R.id.flyplanAdd_fab_close)
    public void onCloseClicked() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            onBackPressed();
        } else {
            backPressed();
        }
    }

    private void backPressed() {
        passDataBack(flyplan);
        super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupExitAnimation() {
        Fade fade = new Fade();
        getWindow().setReturnTransition(fade);
        fade.setDuration(getResources().getInteger(R.integer.animation_duration));
    }

    @Override
    public void onEditSuccess(FlyPlan flyplan) {
        buttonFlyplanAction.setProgress(100); // 100 : Success
        onComplete();
        this.flyplan = flyplan;
        onCloseClicked();
    }

    @Override
    public void onEditFailed() {
        buttonFlyplanAction.setProgress(-1); // -1 : Failed
        this.flyplan = null;
        onComplete();
    }
}
