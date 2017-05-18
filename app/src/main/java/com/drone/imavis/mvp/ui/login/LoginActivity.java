package com.drone.imavis.mvp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.ui.main.MainActivity;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.projects.ProjectsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adigu on 08.05.2017.
 */

public class LoginActivity extends BaseActivity implements ILoginMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.login.LoginActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject LoginPresenter loginPresenter;
    @BindView(R.id.buttonLogin) MorphingButton buttonLogin;
    private Context context;

    private int loginToggleClick = 1;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;

        buttonLogin.setOnClickListener(onClick -> {
            onLoginButtonClicked(buttonLogin);
        });

        loginButtonDefault(buttonLogin, 0);

        loginPresenter.attachView(this);

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }

    private void onLoginButtonClicked(MorphingButton buttonMorph) {
        if (loginToggleClick == 0) {
            loginToggleClick++;
            loginButtonDefault(buttonMorph, 400);
        } else if (loginToggleClick == 1) {
            loginToggleClick = 0;
            buttonLoginSuccess(buttonMorph);
            loginPresenter.login();
        }
    }

    private void loginButtonDefault(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(56)
                .width(200)
                .height(56)
                .color(R.color.mb_blue)
                .colorPressed(R.color.mb_blue_dark)
                .text("Login");
        buttonLogin.morph(square);
    }

    private void buttonLoginSuccess(MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
            .duration(500)
            .cornerRadius(56) // 56 dp
            .width(200) // 56 dp
            .height(56) // 56 dp
            .color(R.color.green) // normal state color
            .colorPressed(R.color.red) // pressed state color
            .text("Success"); // icon
        buttonLogin.morph(circle);
    }

    private void buttonLoginFailure() {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius(56) // 56 dp
                .width(200) // 56 dp
                .height(56) // 56 dp
                .color(R.color.green) // normal state color
                .colorPressed(R.color.red) // pressed state color
                .text("Failure"); // icon
        buttonLogin.morph(circle);
    }

    @Override
    public void onLoginSuccess() {
        goToActivity(this, ProjectsActivity.class, new Bundle());
    }

    @Override
    public void onLoginFailed() {
        Log.i("logginFailed", "todo");
    }
}
