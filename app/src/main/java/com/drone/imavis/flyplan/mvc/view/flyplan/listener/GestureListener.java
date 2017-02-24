package com.drone.imavis.flyplan.mvc.view.flyplan.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;

/**
 * Created by adigu on 23.02.2017.
 */

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    private Coordinate touchCoordinate;

    @Override
    public void onLongPress(MotionEvent event) {
        super.onLongPress(event);
        // int eIndex = MotionEventCompat.getActionIndex(event);
        // check if we've touched inside some node
        Coordinate touchCoordinate = new Coordinate(event.getX(), event.getY());
        Node touchedNode =  (Waypoint) FlyPlanController.getInstance().obtainTouchedNode(touchCoordinate);
        FlyPlanController.getInstance().getFlyPlan().getPoints().addNode(touchedNode);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        // it's the first pointer, so clear all existing pointers data
        //clearCirclePointer();
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        Node touchedNode = (Waypoint) FlyPlanController.getInstance().obtainTouchedNode(touchCoordinate);
        FlyPlanController.getInstance().getFlyPlan().getPoints().addNode(touchedNode);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        return true;
    }
}