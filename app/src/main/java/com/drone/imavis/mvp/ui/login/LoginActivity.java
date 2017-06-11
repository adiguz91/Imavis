package com.drone.imavis.mvp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.remote.webodm.model.Authentication;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.tabs.ProjectsFlyplansActivity;
import com.drone.imavis.mvp.util.ProgressGenerator;
import com.drone.imavis.mvp.util.StringUtil;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.widget.IconTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adigu on 08.05.2017.
 */

public class LoginActivity extends BaseActivity implements ILoginMvpView, ProgressGenerator.OnCompleteListener {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.login.LoginActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject LoginPresenter loginPresenter;
    private ProgressGenerator progressGenerator = new ProgressGenerator(this);
    private Context context;

    @BindView(R.id.iconTextViewLoginAppIcon) IconTextView iconTextViewAppLogo;
    @BindView(R.id.buttonLogin) ActionProcessButton buttonLogin;
    @BindView(R.id.editTextLoginUsername) MaterialEditText textUsername;
    @BindView(R.id.editTextLoginPassword) MaterialEditText textPassword;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, FlyplannerActivity.class);
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

        Typeface fontLobster = Typeface.createFromAsset(getAssets(),  "fonts/Lobster_1.3.ttf");
        iconTextViewAppLogo.setTypeface(fontLobster);

        IconDrawable iconDrawableUsername = new IconDrawable(this, FontAwesomeIcons.fa_user).actionBarSize();
        IconDrawable iconDrawablePassword = new IconDrawable(this, FontAwesomeIcons.fa_lock).actionBarSize();
        textUsername.setIconLeft(iconDrawableUsername);
        textPassword.setIconLeft(iconDrawablePassword);

        buttonLogin.setProgress(0);
        buttonLogin.setMode(ActionProcessButton.Mode.ENDLESS);
        buttonLogin.setOnClickListener(onClick -> login() );
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

    private void login() {

        if (!StringUtil.isNullOrEmpty(textUsername.getEditableText().toString()) &&
            !StringUtil.isNullOrEmpty(textPassword.getEditableText().toString()) ) {

            Authentication authentication =
                    new Authentication(textUsername.getEditableText().toString(),
                                       textPassword.getEditableText().toString());

            buttonLogin.setProgress(1);
            buttonLogin.setEnabled(false);
            textUsername.setEnabled(false);
            textPassword.setEnabled(false);
            loginPresenter.login();
        } else {
            goToActivity(this, FlyplannerActivity.class, new Bundle());
        }
    }

    @Override
    public void onLoginSuccess() {
        buttonLogin.setProgress(100); // 100 : Success
        onComplete();
        goToActivity(this, ProjectsFlyplansActivity.class, new Bundle());

    }

    @Override
    public void onLoginFailed() {
        buttonLogin.setProgress(-1); // -1 : Failed
        onComplete();
    }

    @Override
    public void onComplete() {
        // after button click timeout is reached!
        buttonLogin.setEnabled(true);
        textUsername.setEnabled(true);
        textPassword.setEnabled(true);
    }
}
