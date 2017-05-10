package com.drone.imavis.mvp.services.flyplan.mvc.view.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.drone.imavis.mvp.ui.MainFlyplanner;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;

/**
 * Created by adigu on 23.02.2017.
 */

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    private Coordinate touchCoordinate;
    private Node touchedNode;
    private int pointerId;

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
            }
            return true;
            //else checkSelected(touchedNode.getClass());
        } else
            return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        //super.onLongPress(event);
        FlyPlanView.getNodes().clear();
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));

        // checkIfLongPressLineText
        //if(FlyPlanController.getSelectedWaypoint() != null) {
        Coordinate textLineRectCoor = FlyPlanController.getInstance().isTouchedTextRect(touchCoordinate);
        if(textLineRectCoor != null)
            MainFlyplanner.addActionButtons(textLineRectCoor);
        //}
        else {
            boolean isObtained = FlyPlanController.getInstance().obtainTouchedNode(PointOfInterest.class, touchCoordinate, touchedNode);
            if(touchedNode != null) {
                if(isObtained) {
                    FlyPlanView.getNodes().put(pointerId, touchedNode);
                } else {
                    //checkSelected(touchedNode.getClass());
                    MainFlyplanner.addActionButtons(touchedNode.getShape().getCoordinate());
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