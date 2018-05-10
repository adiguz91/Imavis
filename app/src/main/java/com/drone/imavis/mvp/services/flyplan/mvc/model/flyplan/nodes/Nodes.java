package com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes;

import android.graphics.Canvas;
import android.graphics.Color;

import com.drone.imavis.mvp.data.model.GoogleMapExtension;
import com.drone.imavis.mvp.data.model.SimpleNodes;
import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterests;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.WaypointData;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoints;
import com.drone.imavis.mvp.util.constants.classes.CColor;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Nodes implements Serializable {

    // ANY-ACCESS-MODIFIER
    static final long serialVersionUID = 42L;

    private Waypoints waypoints;
    private PointOfInterests pointOfInterests;

    private GoogleMapExtension map;

    public Nodes() {
        this.waypoints = new Waypoints();
        this.pointOfInterests = new PointOfInterests();
    }

    public void draw(Canvas canvas) {
        int selectedWaypointIndex = getWaypoints().draw(canvas);
        int selectedPoiIndex = getPointOfInterests().draw(canvas);
        int selectedWaypointId = selectedWaypointIndex + 1;
        int selectedPoiId = selectedPoiIndex + 1;
        com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint selectedWaypoint = FlyPlanController.getSelectedWaypoint();
        com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest selectedPOI = FlyPlanController.getSelectedPOI();

        // draw selectedWaypoint
        if (selectedWaypoint != null) {
            selectedWaypoint.setShapeSelectedPaint();
            selectedWaypoint.draw(canvas, String.valueOf(selectedWaypointId), selectedWaypointId);
        }
        // draw selectedPOI
        if (selectedPOI != null) {
            selectedPOI.setShapeSelectedPaint();
            selectedPOI.draw(canvas, String.valueOf(selectedPoiId));
        }
    }

    public PointOfInterest createPoi(Coordinate touchCoordinate) {
        if (getPointOfInterests() == null) {
            PointOfInterest poi = new PointOfInterest(touchCoordinate);
            poi.getShape().setBackgroundColor(Color.parseColor(CColor.POI_CIRCLES.get(0)));
            return poi;
        }
        if (getPointOfInterests().size() >= CColor.POI_CIRCLES.size())
            return null;

        boolean isColorFound;
        for (String color : CColor.POI_CIRCLES) {
            isColorFound = false;
            for (PointOfInterest poi : getPointOfInterests()) {
                if (Color.parseColor(color) == poi.getShape().getBackgroundColor()) {
                    isColorFound = true;
                    break;
                }
            }
            if (!isColorFound) {
                PointOfInterest poi = new PointOfInterest(touchCoordinate);
                poi.getShape().setBackgroundColor(Color.parseColor(color));
                return poi;
            }
        }
        return null;
    }

    public void loadNodes(String simpleNodesJson) {
        if (map == null)
            return;

        SimpleNodes simpleNodes = new Gson().fromJson(simpleNodesJson, SimpleNodes.class);
        for (com.drone.imavis.mvp.data.model.Waypoint waypoint : simpleNodes.getWaypoints()) {
            Coordinate screenCoordinate = map.getScreenCoordinatesfromGps(waypoint.getGpsCoordinate());
            if (screenCoordinate == null) // try again
                screenCoordinate = map.getScreenCoordinatesfromGps(waypoint.getGpsCoordinate());
            Waypoint waypointNode = new Waypoint(screenCoordinate);
            waypoints.add(waypointNode);
        }
        for (com.drone.imavis.mvp.data.model.PointOfInterest pointOfInterest : simpleNodes.getPointOfInterests()) {
            Coordinate screenCoordinate = map.getScreenCoordinatesfromGps(pointOfInterest.getGpsCoordinate());
            if (screenCoordinate == null) // try again
                screenCoordinate = map.getScreenCoordinatesfromGps(pointOfInterest.getGpsCoordinate());
            PointOfInterest poiNode = new PointOfInterest(screenCoordinate);
            pointOfInterests.add(poiNode);
        }
    }

    private SimpleNodes toSimpleNodes() {
        SimpleNodes simpleNodes = new SimpleNodes();
        List<com.drone.imavis.mvp.data.model.Waypoint> waypointsSimple = new ArrayList();
        List<com.drone.imavis.mvp.data.model.PointOfInterest> poisSimple = new ArrayList();
        for (ListIterator<Waypoint> it = waypoints.listIterator(); it.hasNext(); ) {
            Waypoint waypointNode = it.next();
            com.drone.imavis.mvp.data.model.Waypoint waypoint = new com.drone.imavis.mvp.data.model.Waypoint();
            GPSCoordinate currentGpsCoordinate = waypointNode.getShape().getCoordinate().getGpsCoordinate();
            if (currentGpsCoordinate == null)
                currentGpsCoordinate = map.getGpsfromScreen(waypointNode.getShape().getCoordinate());
            waypoint.setGpsCoordinate(currentGpsCoordinate);
            waypointsSimple.add(waypoint);
        }
        for (ListIterator<PointOfInterest> it = pointOfInterests.listIterator(); it.hasNext(); ) {
            PointOfInterest pointOfInterestNode = it.next();
            com.drone.imavis.mvp.data.model.PointOfInterest pointOfInterest = new com.drone.imavis.mvp.data.model.PointOfInterest();
            GPSCoordinate currentGpsCoordinate = pointOfInterestNode.getShape().getCoordinate().getGpsCoordinate();
            if (currentGpsCoordinate == null)
                currentGpsCoordinate = map.getGpsfromScreen(pointOfInterestNode.getShape().getCoordinate());
            pointOfInterest.setGpsCoordinate(currentGpsCoordinate);
            poisSimple.add(pointOfInterest);
        }
        simpleNodes.setWaypoints(waypointsSimple);
        simpleNodes.setPointOfInterests(poisSimple);
        return simpleNodes;
    }

    public String toSimpleNodesJson() {
        return new Gson().toJson(toSimpleNodes());
    }

    public void setMap(GoogleMapExtension map) {
        this.map = map;
    }

    public Waypoints getWaypoints() {
        return waypoints;
    }

    public PointOfInterests getPointOfInterests() {
        return pointOfInterests;
    }

    public void editNode(Node node) {
        setGPSCoordinateFromNode(node);
        if (node.getClass() == Waypoint.class) {
            int nodeIndex = getWaypoints().indexOf(node);
            getWaypoints().set(nodeIndex, (Waypoint) node);
        } else {
            int nodeIndex = getPointOfInterests().indexOf(node);
            getPointOfInterests().set(nodeIndex, (PointOfInterest) node);
        }
    }

    private void setGPSCoordinateFromNode(Node node) {
        if (map != null) {
            GPSCoordinate gpsCoordinate = map.getGpsfromScreen(node.getShape().getCoordinate());
            node.getShape().getCoordinate().setGpsCoordinate(gpsCoordinate);
        }
    }

    public void addNode(Node node) {
        setGPSCoordinateFromNode(node);
        if (node.getClass() == Waypoint.class) {
            getWaypoints().add((Waypoint) node);
        } else {
            getPointOfInterests().add((PointOfInterest) node);
        }
    }

    public boolean removeNode(Node node) {
        setGPSCoordinateFromNode(node);
        boolean isRemoved = false;
        if (node.getClass() == Waypoint.class)
            isRemoved = getWaypoints().remove(node);
        else {
            isRemoved = getPointOfInterests().remove(node);
            if (isRemoved) {
                if (node.equals(FlyPlanController.getSelectedPOI()))
                    FlyPlanController.setSelectedPOI(null);

                ListIterator<Waypoint> iterator = getWaypoints().listIterator();
                while (iterator.hasNext()) {
                    Waypoint waypoint = iterator.next();
                    PointOfInterest poi = ((WaypointData) waypoint.getData()).getPoi();
                    if (node.equals(poi))
                        ((WaypointData) waypoint.getData()).setPoi(null);
                }
            }
        }

        if (isRemoved) {
            if (node.equals(FlyPlanController.getSelectedWaypoint()))
                FlyPlanController.setSelectedWaypoint(null);
        }

        return isRemoved;
    }


}
