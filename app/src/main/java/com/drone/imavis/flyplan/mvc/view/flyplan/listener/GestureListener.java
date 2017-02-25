package com.drone.imavis.flyplan.mvc.view.flyplan.listener;

import android.support.v4.view.MotionEventCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.poi.PointOfInterest;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.flyplan.mvc.view.flyplan.FlyPlanView;

/**
 * Created by adigu on 23.02.2017.
 */

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    private Coordinate touchCoordinate;
    private Node touchedNode;
    private int pointerId;

    @Override
    public boolean onDown(MotionEvent event) {
        //FlyPlanView.getNodes().clear();
        //pointerId = event.getPointerId(0);
        //touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        //touchedNode = (Waypoint) FlyPlanController.getInstance().obtainTouchedNode(touchCoordinate);
        //FlyPlanView.getNodes().put(pointerId, touchedNode);
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //FlyPlanView.getNodes().clear();
        //return true;

        FlyPlanView.getNodes().clear();
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        touchedNode = (Waypoint) FlyPlanController.getInstance().obtainTouchedNode(Waypoint.class, touchCoordinate);
        FlyPlanView.getNodes().put(pointerId, touchedNode);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        //super.onLongPress(event);
        FlyPlanView.getNodes().clear();
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        touchedNode = (PointOfInterest) FlyPlanController.getInstance().obtainTouchedNode(PointOfInterest.class, touchCoordinate);
        FlyPlanView.getNodes().put(pointerId, touchedNode);
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

}