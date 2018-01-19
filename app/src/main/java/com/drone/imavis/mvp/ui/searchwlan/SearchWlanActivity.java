package com.drone.imavis.mvp.ui.searchwlan;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adigu on 08.05.2017.
 */

public class SearchWlanActivity extends BaseActivity {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.drone.imavis.mvp.ui.searchwlan.SearchWlanActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject SearchWlanPresenter searchWlanPresenter;
    private Context context;

    @BindView(R.id.wlanMap) RippleBackground wlanMapView;
    @BindView(R.id.foundDevice) ImageView foundDevice;
    @BindView(R.id.centerImage) ImageView centerImageButton;
    final Handler wlanMapHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_searchwlan);
        ButterKnife.bind(this);
        context = this;

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            //startService(SyncService.getStartIntent(this));
        }

        load();
    }

    private void load() {
        searchWlans();
    }

    private void searchWlans() {
        centerImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wlanMapView.startRippleAnimation();
                wlanMapHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        foundDevice();
                    }
                },5000);
            }
        });
    }

    private void foundDevice(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        foundDevice.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchWlanPresenter.detachView();
    }
}


