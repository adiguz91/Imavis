package com.drone.imavis.mvp.services.flyplan.mvc.view.listener;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Circle;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.shapes.simple.Line;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoints;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by adigu on 23.02.2017.
 */

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    private Coordinate touchCoordinate;
    private Node touchedNode;
    private int pointerId;
    private FlyPlanView parentView;

    public GestureListener(FlyPlanView parentView) {
        this.parentView = parentView;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        FlyPlanView.getNodes().clear();
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        touchedNode = FlyPlanController.getInstance().getTouchedNode(touchCoordinate);
        //if(touchedNode == null)
        //    return false;
        return true;
    }
    
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //FlyPlanView.getNodes().clear();
        //return true;

        FlyPlanView.getNodes().clear();
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        boolean isObtained = FlyPlanController.getInstance().obtainTouchedNode(Waypoint.class, touchCoordinate, touchedNode);
        if(touchedNode != null) {
            if(isObtained) {
                FlyPlanView.getNodes().put(pointerId, touchedNode);
               // mFlyPlanView.
            }
            return true;
            //else checkSelected(touchedNode.getClass());
        } else
            return false;
    }

    private LinearLayout.LayoutParams fabSheetCardViewLayout;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onLongPress(MotionEvent event) {
        //super.onLongPress(event);
        FlyPlanView.getNodes().clear();
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));

        // checkIfLongPressLineText
        //if(FlyPlanController.getSelectedWaypoint() != null) {
        Coordinate textLineRectCoor = FlyPlanController.getInstance().isTouchedTextRect(touchCoordinate);

        if(textLineRectCoor != null) {
            // ##################################################
            // MainFlyplanner.addActionButtons(textLineRectCoor);
            // ##################################################
        }
        else {
            boolean isObtained = FlyPlanController.getInstance().obtainTouchedNode(PointOfInterest.class, touchCoordinate, touchedNode);
            if(touchedNode != null) {
                if(isObtained) {
                    FlyPlanView.getNodes().put(pointerId, touchedNode);
                } else {
                    // ACTION BUTTONs open
                    ((FlyplannerActivity)parentView.getContext()).getActionFabSheet().setVisibility(View.VISIBLE);
                    float radius = ((Circle)(touchedNode).getShape()).getRadius();
                    LinearLayout fabSheetItemClose = ((Activity)parentView.getContext()).findViewById(R.id.fabSheetItemClose);
                    if(touchedNode.getClass().equals(Waypoint.class)) {
                        // Waypoint
                        fabSheetItemClose.setVisibility(View.VISIBLE);
                    } else {
                        // POI
                        fabSheetItemClose.setVisibility(View.INVISIBLE);
                    }
                    Coordinate centeredCoordinate = new Coordinate(touchedNode.getShape().getCoordinate().getX() - radius, touchedNode.getShape().getCoordinate().getY() - radius);
                    ((FlyplannerActivity)parentView.getContext()).getActionFabSheetMenu().showFab(centeredCoordinate.getX(), centeredCoordinate.getY());

                    //((FlyplannerActivity)parentView.getContext()).getActionFabSheet().performContextClick(touchedNode.getShape().getCoordinate().getX(), touchedNode.getShape().getCoordinate().getY());
                    CardView fabSheedCardView = ((FlyplannerActivity)parentView.getContext()).findViewById(R.id.fabSheetCardView);

                    if (fabSheetCardViewLayout == null)
                        fabSheetCardViewLayout = (LinearLayout.LayoutParams) fabSheedCardView.getLayoutParams();

                    LinearLayout.LayoutParams layoutParams = fabSheetCardViewLayout;
                    layoutParams.setMargins((int)centeredCoordinate.getX(), (int)centeredCoordinate.getY(), 0, 0);
                    ((FlyplannerActivity)parentView.getContext()).getActionFabSheetCardView().setLayoutParams(layoutParams);
                    ((FlyplannerActivity)parentView.getContext()).getActionFabSheetMenu().showSheet();
                }
            }
        }

    }
/*


    @Override
    public void onShowPress(MotionEvent event) {
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        return true;
    }
*/

    /*
    public void checkSelected(Class classname) {
        if (classname == Waypoint.class) {
            selectedPOI = null;
            selectedWaypoint = touchedNode;
        } else if (classname == PointOfInterest.class){
            selectedWaypoint = null;
            selectedPOI = touchedNode;
        }
    }
    */
}