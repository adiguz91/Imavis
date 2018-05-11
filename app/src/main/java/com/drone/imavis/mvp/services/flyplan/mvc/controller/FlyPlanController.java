package com.drone.imavis.mvp.services.flyplan.mvc.controller;

import android.graphics.Canvas;

import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.WaypointData;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.util.FileUtil;
import com.drone.imavis.mvp.util.constants.classes.CFileDirectories;
import com.drone.imavis.mvp.util.constants.classes.CFiles;
import com.drone.imavis.mvp.util.constants.classes.CFlyPlan;
import com.drone.imavis.mvp.util.constants.classes.CShape;

import java.io.File;
import java.util.ListIterator;

/**
 * Created by adigu on 23.02.2017.
 */

public class FlyPlanController implements IFlyPlan {

    private static FlyPlanController flyPlanController;
    private Node touchedNode;
    private Waypoint selectedWaypoint;
    private PointOfInterest selectedPOI;
    private FlyPlanView parent;

    public FlyPlanController(FlyPlanView parent) {
        this.parent = parent;
        flyPlanController = this;
    }

    // SINGLETON PATTERN
    public static FlyPlanController getInstance() {
        return flyPlanController;
    }

    public Waypoint getSelectedWaypoint() {
        return selectedWaypoint;
    }

    public void setSelectedWaypoint(Node touchedNode) {
        if (touchedNode == null)
            selectedWaypoint = null;
        else {
            if (touchedNode.getClass() == Waypoint.class) {
                if (getSelectedWaypoint() == touchedNode) {
                    removePoiFromWaypoint((Waypoint) touchedNode);
                    selectedWaypoint = null; // double de/selected node
                } else {
                    selectedWaypoint = (Waypoint) touchedNode;
                    addPoiToWaypoint((Waypoint) touchedNode);
                }
            }
        }
    }

    public PointOfInterest getSelectedPOI() {
        return selectedPOI;
    }

    public void setSelectedPOI(Node node) {
        if (node == null) {
            selectedPOI = null;
        } else {
            if (node.getClass() == PointOfInterest.class) {
                if (selectedPOI == null) {
                    selectedPOI = (PointOfInterest) node;
                } else if (getSelectedPOI() == node)
                    selectedPOI = null; // double de/selected node
                selectedWaypoint = null;
            }
        }
    }

    public Node getTouchedNode() {
        return touchedNode;
    }

    private void setTouchedNode(Node node) {
        touchedNode = node;
    }

    private void setSelectedNode(Node touchedNode) {
        setTouchedNode(touchedNode);
        setSelectedPOI(touchedNode);
        setSelectedWaypoint(touchedNode);
    }

    /**
     * Search and creates new (if needed) node based on touch area
     *
     * @param touchCoordinate
     * @return obtained {@link Node}
     */
    public boolean obtainTouchedNode(Class className, Coordinate touchCoordinate, Node touchedNode) {
        touchedNode = getTouchedNode(touchCoordinate);

        if (touchedNode == null) {
            if (className == Waypoint.class) {
                touchedNode = new Waypoint(touchCoordinate);
                if (getFlyPlan().getPoints().getWaypoints().size() == CFlyPlan.MAX_WAYPOINTS_SIZE) {
                    getFlyPlan().getPoints().getWaypoints().clear();
                }
            }
            if (className == PointOfInterest.class) {
                touchedNode = getFlyPlan().getPoints().createPoi(touchCoordinate);
                if (getFlyPlan().getPoints().getPointOfInterests().size() == CFlyPlan.MAX_POI_SIZE) {
                    getFlyPlan().getPoints().getPointOfInterests().clear();
                }
            }
            //Log.w(TAG, "Added node " + touchedNode);
            getFlyPlan().getPoints().addNode(touchedNode);
            setSelectedNode(touchedNode);
            return true;
        }
        touchedNode.getShape().setCoordinate(touchCoordinate);
        setSelectedNode(touchedNode);
        return false;
    }

    private void addPoiToWaypoint(Waypoint touchedWaypoint) {
        if (getSelectedPOI() != null) {
            int toucheWpIndex = getFlyPlan().getPoints().getWaypoints().indexOf(touchedWaypoint);
            Waypoint waypoint = getFlyPlan().getPoints().getWaypoints().get(toucheWpIndex);
            ((WaypointData) waypoint.getData()).setPoi(getSelectedPOI());
            getFlyPlan().getPoints().getWaypoints().set(toucheWpIndex, waypoint);
        }
    }

    private void removePoiFromWaypoint(Waypoint touchedWaypoint) {
        if (getSelectedPOI() != null && touchedWaypoint != null) {
            int toucheWpIndex = getFlyPlan().getPoints().getWaypoints().indexOf(touchedWaypoint);
            Waypoint waypoint = getFlyPlan().getPoints().getWaypoints().get(toucheWpIndex);
            ((WaypointData) waypoint.getData()).setPoi(null);
            getFlyPlan().getPoints().getWaypoints().set(toucheWpIndex, waypoint);
        }
    }

    public void draw(Canvas canvas) {
        if (getFlyPlan() != null)
            getFlyPlan().draw(canvas);
    }

    public float getScaleFactor() {
        return parent.getScaleFactor();
    }

    /**
     * Determines touched node
     *
     * @param touchCoordinate
     * @return {@link Node} touched node or null if no node has been touched
     */
    public Node getTouchedNode(Coordinate touchCoordinate) {
        Node touched = null;
        Waypoint node;
        ListIterator iterator;

        // find waypoints
        iterator = getFlyPlan().getPoints().getWaypoints().listIterator();
        while (iterator.hasNext()) {
            node = (Waypoint) iterator.next();
            if (findNodeFromPoint(node.getShape().getCoordinate(), touchCoordinate, CShape.WAYPOINT_CIRCLE_RADIUS)) {
                touched = node;
                break;
            }
        }

        // find pointOfInterest
        if (touched == null) {
            for (PointOfInterest poi : getFlyPlan().getPoints().getPointOfInterests()) {
                if (findNodeFromPoint(poi.getShape().getCoordinate(), touchCoordinate, CShape.POI_CIRCLE_RADIUS)) {
                    touched = poi;
                    break;
                }
            }
        }

        if (touched != null)
            touched.getShape().setCoordinate(touchCoordinate);
        setSelectedNode(touched);
        return touched;
    }

    public Coordinate isTouchedTextRect(Coordinate touchCoordinate) {
        Waypoint node;
        ListIterator iterator;
        Coordinate foundRectCoord = null;

        // find waypoints
        iterator = getFlyPlan().getPoints().getWaypoints().listIterator();
        while (iterator.hasNext()) {
            node = (Waypoint) iterator.next();
            if (node.getLineTextRect() != null && node.getLineTextRect().contains(
                    (int) touchCoordinate.getX(), (int) touchCoordinate.getY())) {
                foundRectCoord = touchCoordinate;
                break;
            }
        }
        return foundRectCoord;
    }

    @Override
    public FlyPlan getFlyPlan() {
        return parent.getFlyplannerFragment().getFlyPlan();
    }

    private boolean findNodeFromPoint(Coordinate nodeCoordinate, Coordinate touchedCoordinate, int radius) {
        return (Math.pow((nodeCoordinate.getX() - touchedCoordinate.getX()), 2) +
                Math.pow((nodeCoordinate.getY() - touchedCoordinate.getY()), 2))
                < Math.pow(radius, 2);
    }

    @Override
    public FlyPlan onPlanCreateNew() {
        //GPSCoordinate coordinate = new GPSCoordinate(0, 0);
        //return new FlyPlan(new MapData(coordinate, 1.0f));
        return null;
    }

    @Override
    public FlyPlan onPlanLoad(File file) {
        try {
            return FlyPlan.loadFromJsonFile(FileUtil.readFile(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onPlanSave() {
        String filenameWithExtension = getFlyPlan().getName() + CFiles.SAVE_DATATYPE;
        String absoluteFlyPlanFilePath = FileUtil.FilePathCombine(CFileDirectories.FLYPLAN_ABSOLUTE, filenameWithExtension);
        return FileUtil.writeToFile(absoluteFlyPlanFilePath, getFlyPlan().saveToJsonFile());
    }

    @Override
    public boolean onPlanDelete() {
        return false;
    }

    @Override
    public boolean onNodeAdd(Coordinate coordinate) {
        return false;
    }

    @Override
    public Node onNodeSelect(Coordinate coordinate) {
        return null;
    }

    @Override
    public boolean onNodeDelete(Node node) {
        return false;
    }

    @Override
    public boolean onNodeSelectAction(Node node) {
        return false;
    }
}
