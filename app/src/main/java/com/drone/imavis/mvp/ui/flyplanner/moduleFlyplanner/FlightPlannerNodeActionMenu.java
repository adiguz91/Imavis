package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.services.flyplan.mvc.view.SheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FlightPlannerNodeActionMenu extends RelativeLayout {

    @BindView(R.id.flyplannerDraw)
    FlyPlanView flyplannerDrawer;
    @BindView(R.id.fabSheetWaypoint)
    SheetFab fabSheetWaypoint;
    @BindView(R.id.fabSheetCardViewWaypoint)
    CardView sheetViewWaypoint;
    @BindView(R.id.overlayWaypoint)
    View overlayWaypoint;
    private MaterialSheetFab actionFabSheetMenuWaypoint;
    private Unbinder unbinder;

    public FlightPlannerNodeActionMenu(Context context) {
        super(context);
        onLayoutInflate(context);
    }

    public FlightPlannerNodeActionMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onLayoutInflate(context);
    }

    public FlightPlannerNodeActionMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onLayoutInflate(context);
    }

    public FlightPlannerNodeActionMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        View view = inflater.inflate(R.layout.flightplanner_nodeactionmenu, this);
        //ButterKnife.bind(this, view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //((BaseActivity) getContext()).activityComponent().inject(this);
        // before initialize
    }

    /**
     * MUST BE CALLED MANUALLY BEFORE USED
     */
    public void initialize() {
        unbinder = ButterKnife.bind(this, getRootView());
        initActionMenu();
    }

    private void initActionMenu() {
        // Initialize material sheet FAB
        int sheetColor = getContext().getResources().getColor(R.color.transparent);
        int fabColor = getContext().getResources().getColor(R.color.transparent);
        fabSheetWaypoint.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.transparent)));
        fabSheetWaypoint.setRippleColor(getContext().getResources().getColor(R.color.transparent));
        fabSheetWaypoint.setElevation(0);
        fabSheetWaypoint.setCompatElevation(0);
        actionFabSheetMenuWaypoint = new MaterialSheetFab<>(fabSheetWaypoint, sheetViewWaypoint, overlayWaypoint, sheetColor, fabColor);
        actionFabSheetMenuWaypoint.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Called when the material sheet's "show" animation starts.
            }

            @Override
            public void onSheetShown() {
                // Called when the material sheet's "show" animation ends.
            }

            @Override
            public void onHideSheet() {
                // Called when the material sheet's "hide" animation starts.

            }

            public void onSheetHidden() {
                // Called when the material sheet's "hide" animation ends.
                flyplannerDrawer.setIsEnabledActionMenu(false);
                fabSheetWaypoint.setVisibility(View.GONE);
            }
        });
        fabSheetWaypoint.setVisibility(View.INVISIBLE);
    }

    public MaterialSheetFab getActionFabSheetMenuWaypoint() {
        return actionFabSheetMenuWaypoint;
    }

    public SheetFab getActionFabSheetWaypoint() {
        return fabSheetWaypoint;
    }

    public CardView getActionFabSheetCardViewWaypoint() {
        return sheetViewWaypoint;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow(); // "onDestroy" code here
        unbinder.unbind();
    }
}
