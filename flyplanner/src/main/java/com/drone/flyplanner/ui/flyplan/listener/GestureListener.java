package com.drone.flyplanner.ui.flyplan.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.drone.flyplanner.DaggerFlyplanner;
import com.drone.flyplanner.data.model.flyplan.nodes.Node;
import com.drone.flyplanner.data.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.flyplanner.data.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.flyplanner.ui.flyplan.FlyPlanView;
import com.drone.flyplanner.util.flyplan.control.FlyPlanUtil;
import com.drone.flyplanner.util.flyplan.control.IFlyPlanUtil;
import com.drone.flyplanner.util.models.coordinates.Coordinate;

import javax.inject.Inject;

/**
 * Created by adigu on 23.02.2017.
 */

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    //@Inject
    FlyPlanView flyPlanView;

    //@Inject
    IFlyPlanUtil flyPlanUtil = FlyPlanUtil.getInstance();

    private Coordinate touchCoordinate;
    private Node touchedNode;
    private int pointerId;

    public GestureListener(FlyPlanView flyPlanView) {
        this.flyPlanView = flyPlanView;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        flyPlanView.getNodes().clear();
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        touchedNode = flyPlanUtil.getTouchedNode(touchCoordinate);
        //if(touchedNode == null)
        //    return false;
        return true;
    }
    
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //FlyPlanView.getNodes().clear();
        //return true;

        flyPlanView.getNodes().clear();
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        boolean isObtained = flyPlanUtil.obtainTouchedNode(Waypoint.class, touchCoordinate, touchedNode);
        if(touchedNode != null) {
            if(isObtained) {
                flyPlanView.getNodes().put(pointerId, touchedNode);
            }
            return true;
            //else checkSelected(touchedNode.getClass());
        } else
            return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        //super.onLongPress(event);
        flyPlanView.getNodes().clear();
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));

        // checkIfLongPressLineText
        //if(FlyPlanController.getSelectedWaypoint() != null) {
        Coordinate textLineRectCoor = flyPlanUtil.isTouchedTextRect(touchCoordinate);

        if(textLineRectCoor != null) {
            // ##################################################
            // MainFlyplanner.addActionButtons(textLineRectCoor);
            // ##################################################
        }
        else {
            boolean isObtained = flyPlanUtil.obtainTouchedNode(PointOfInterest.class, touchCoordinate, touchedNode);
            if(touchedNode != null) {
                if(isObtained) {
                    flyPlanView.getNodes().put(pointerId, touchedNode);
                } else {
                    //checkSelected(touchedNode.getClass());
                    // ##################################################
                    //MainFlyplanner.addActionButtons(touchedNode.getShape().getCoordinate());
                    // ##################################################
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