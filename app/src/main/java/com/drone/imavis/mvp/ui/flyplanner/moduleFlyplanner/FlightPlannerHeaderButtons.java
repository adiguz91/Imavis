package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.github.clans.fab.FloatingActionMenu;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.android.gms.maps.GoogleMap;
import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FlightPlannerHeaderButtons extends RelativeLayout {

    private static final int ANIMATION_DURATION = 300;
    private static final float ROTATION_ANGLE = -45f;
    @BindView(R.id.flyplannerDraw)
    FlyPlanView flyplannerDrawer;
    // HEADER MenuButtons
    @BindView(R.id.flyplanner_fab_mapType_menu)
    FloatingActionMenu fabMapTypeMenu;
    @BindView(R.id.flyplanner_fab_back)
    FloatingActionButton fabBack;
    @BindView(R.id.flyplanner_fab_droneCalibration)
    FloatingActionButton fabDroneCalibration;
    @BindView(R.id.flyplanner_fab_currentGpsPosition)
    FloatingActionButton fabCurrentGpsPosition;
    @BindView(R.id.flyplanner_fab_droneConnectWifi)
    FloatingActionButton fabDroneConnectWifi;
    @BindView(R.id.flyplanner_fab_flyplanSettings)
    FloatingActionButton fabFlyplanSettings;
    @BindView(R.id.flyplanner_fab_mapLock)
    FloatingActionButton fabMapLock;
    @BindView(R.id.flyplanner_fab_mapType_menu_terrain)
    com.github.clans.fab.FloatingActionButton fabMapTypeTerrain;
    @BindView(R.id.flyplanner_fab_mapType_menu_satellite)
    com.github.clans.fab.FloatingActionButton fabMapTypeSatellite;
    private boolean mapIsLocked;
    private AnimatorSet mOpenAnimatorSet;
    private AnimatorSet mCloseAnimatorSet;
    private FlyplannerFragment flyplannerFragment;
    private Unbinder unbinder;

    public FlightPlannerHeaderButtons(Context context) {
        super(context);
        onLayoutInflate(context);
    }

    public FlightPlannerHeaderButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
        onLayoutInflate(context);
    }

    public FlightPlannerHeaderButtons(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onLayoutInflate(context);
    }

    public FlightPlannerHeaderButtons(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onLayoutInflate(context);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context: the current context for the view.
     */
    private void onLayoutInflate(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.flightplanner_headerbuttons, this);
        //ButterKnife.bind(this, view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // before initialize
        flyplannerFragment = (FlyplannerFragment) ((BaseActivity) getContext()).getSupportFragmentManager().findFragmentById(R.id.flyplanner);
        mapIsLocked = false;
    }

    /**
     * MUST BE CALLED MANUALLY BEFORE USED
     */
    public void initialize() {
        unbinder = ButterKnife.bind(this, getRootView());
        initHeader();
    }

    private void initHeader() {
        fabMapTypeMenu.getMenuIconView().setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_map)
                .colorRes(R.color.icons)
                .actionBarSize());

        fabBack.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_arrow_left)
                .colorRes(R.color.icons)
                .actionBarSize());

        fabCurrentGpsPosition.setImageDrawable(
                new IconDrawable(getContext(), FontAwesomeIcons.fa_crosshairs)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        Icon lockingIcon = FontAwesomeIcons.fa_unlock_alt;
        int ressourceColor = R.color.md_yellow_400;
        if (mapIsLocked) {
            lockingIcon = FontAwesomeIcons.fa_lock;
            ressourceColor = R.color.md_red_400;
        }

        fabMapLock.setImageDrawable(new IconDrawable(getContext(), lockingIcon)
                .colorRes(ressourceColor)
                .actionBarSize());

        int wifiColor = R.color.md_red_400;
        /*if (isWifiDroneConnectionActive(lasKnownSSID)) {
            wifiColor = R.color.md_green_400;
        }*/

        fabDroneConnectWifi.setImageDrawable(
                new IconDrawable(getContext(), FontAwesomeIcons.fa_wifi)
                        .colorRes(wifiColor)
                        .actionBarSize());

        fabFlyplanSettings.setImageDrawable(
                new IconDrawable(getContext(), FontAwesomeIcons.fa_cog)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        ViewTooltip.on(((Activity) getContext()), fabDroneCalibration)
                .corner(30)
                .autoHide(false, 1000)
                .position(ViewTooltip.Position.BOTTOM)
                .text("Calibrate!")
                .clickToHide(false)
                .textColor(Color.WHITE)
                .color(getContext().getResources().getColor(R.color.colorPrimary))
                .show();

        setUpCustomFabMenuAnimation();
        if (fabMapTypeMenu.isOpened())
            fabMapTypeMenu.setClickable(true);
        else
            fabMapTypeMenu.setClickable(false);
    }

    @OnClick(R.id.flyplanner_fab_mapLock)
    public void onFabClickMapLock(FloatingActionButton button) {
        mapIsLocked = !mapIsLocked;
        if (flyplannerDrawer != null)
            flyplannerDrawer.setIsLocked(mapIsLocked);

        Icon lockingIcon = FontAwesomeIcons.fa_unlock_alt;
        int ressourceColor = R.color.md_yellow_400;
        if (mapIsLocked) {
            lockingIcon = FontAwesomeIcons.fa_lock;
            ressourceColor = R.color.md_red_400;
        }
        fabMapLock.setImageDrawable(new IconDrawable(getContext(), lockingIcon)
                .colorRes(ressourceColor)
                .actionBarSize());
    }

    @OnClick(R.id.flyplanner_fab_mapType_menu_satellite)
    public void onMapTypeSatelliteClicked(com.github.clans.fab.FloatingActionButton button) {
        flyplannerFragment.getGoogleMapFragment().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        fabMapTypeMenu.close(true);
    }

    @OnClick(R.id.flyplanner_fab_mapType_menu_terrain)
    public void onMapTypeTerrainClicked(com.github.clans.fab.FloatingActionButton button) {
        flyplannerFragment.getGoogleMapFragment().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        fabMapTypeMenu.close(true);
    }

    private void setUpCustomFabMenuAnimation() {
        mOpenAnimatorSet = new AnimatorSet();
        mCloseAnimatorSet = new AnimatorSet();

        ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(fabMapTypeMenu.getMenuIconView(),
                "rotation",
                -90f + ROTATION_ANGLE, 0f);
        ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(fabMapTypeMenu.getMenuIconView(),
                "rotation",
                0f, -90f + ROTATION_ANGLE);

        expandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // this will be rotated so that the plus icon will be seen as "x"
                fabMapTypeMenu.getMenuIconView().setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_plus)
                        .colorRes(R.color.icons)
                        .actionBarSize());
                fabMapTypeMenu.setIconToggleAnimatorSet(mCloseAnimatorSet);
                fabMapTypeMenu.setClickable(true);
            }
        });

        collapseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fabMapTypeMenu.getMenuIconView().setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_map)
                        .colorRes(R.color.icons)
                        .actionBarSize());
                fabMapTypeMenu.setIconToggleAnimatorSet(mOpenAnimatorSet);
                fabMapTypeMenu.setClickable(false);
            }
        });

        mOpenAnimatorSet.play(expandAnimator);
        mCloseAnimatorSet.play(collapseAnimator);

        mOpenAnimatorSet.setDuration(ANIMATION_DURATION);
        mCloseAnimatorSet.setDuration(ANIMATION_DURATION);

        fabMapTypeMenu.setIconToggleAnimatorSet(mOpenAnimatorSet);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow(); // "onDestroy" code here
        unbinder.unbind();
    }
}
