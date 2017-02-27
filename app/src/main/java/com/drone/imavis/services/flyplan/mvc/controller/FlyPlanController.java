package com.drone.imavis.services.flyplan.mvc.controller;

import android.graphics.Canvas;
import android.graphics.Color;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CFileDirectories;
import com.drone.imavis.constants.classes.CFiles;
import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.FlyPlan;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.map.Map;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.extensions.dimension.Size;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Circle;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.WaypointData;
import com.drone.imavis.services.flyplan.mvc.view.FlyPlanView;
import com.google.android.gms.maps.GoogleMap;

import java.io.File;
import java.util.ListIterator;

import com.drone.imavis.extensions.file.FileExtension;

/**
 * Created by adigu on 23.02.2017.
 */
public class FlyPlanController implements IFlyPlan {

    private static FlyPlanController flyPlanController;
    private FlyPlan flyPlan;

    private static Waypoint selectedWaypoint;
    public static Waypoint getSelectedWaypoint() { return selectedWaypoint; }

    private static PointOfInterest selectedPOI;
    public static PointOfInterest getSelectedPOI() { return selectedPOI; }

    //private static Node touchedNodeGlobalLast;
    //public static Node getTouchedNodeLast() { return touchedNodeGlobalLast; }

    // SINGLETON PATTERN
    public static FlyPlanController getInstance() {
        if (flyPlanController == null)
            flyPlanController = new FlyPlanController();
        return flyPlanController;
    }

    public FlyPlanController() {
        Coordinate mapCoordinate = new Coordinate(0,0);
        Size mapSize = new Size(50, 50);
        Map map = new Map<GoogleMap>(mapCoordinate, mapSize);
        this.flyPlan = new FlyPlan(map);
    }
    public FlyPlanController(Map map) {
        this.flyPlan = new FlyPlan(map);
    }
    public FlyPlanController(FlyPlan flyPlan) {
        this.flyPlan = flyPlan;
    }

    /**
     * Search and creates new (if needed) node based on touch area
     * @param touchCoordinate
     * @return obtained {@link Node}
     */
    public boolean obtainTouchedNode(Class className, Coordinate touchCoordinate, Node touchedNode) {
        touchedNode = getTouchedNode(touchCoordinate);

        if (touchedNode == null) {
            if(className == Waypoint.class) {
                touchedNode = new Waypoint(touchCoordinate);
                if (flyPlan.getPoints().getWaypoints().size() == CFlyPlan.MAX_WAYPOINTS_SIZE) {
                    flyPlan.getPoints().getWaypoints().clear();
                }
            }
            if(className == PointOfInterest.class) {
                touchedNode = new PointOfInterest(touchCoordinate);
                if (flyPlan.getPoints().getPointOfInterests().size() == CFlyPlan.MAX_POI_SIZE) {
                    flyPlan.getPoints().getPointOfInterests().clear();
                }
            }
            //Log.w(TAG, "Added node " + touchedNode);
            flyPlan.getPoints().addNode(touchedNode);
            setSelectedNode(touchedNode);
            return true;
        }
        touchedNode.getShape().setCoordinate(touchCoordinate);
        setSelectedNode(touchedNode);
        return false;
    }

    private static Node touchedNode;
    public static Node getTouchedNode() {
        return touchedNode;
    }
    private static void setTouchedNode(Node node) {
        touchedNode = node;
    }

    private void setSelectedNode(Node touchedNode) {
        setTouchedNode(touchedNode);
        setSelectedPOI(touchedNode);
        setSelectedWaypoint(touchedNode);
    }

    private void setSelectedWaypoint(Node touchedNode) {
        if(touchedNode == null)
            selectedWaypoint = null;
        else {
            if(touchedNode.getClass() == Waypoint.class) {
                if(getSelectedWaypoint() == touchedNode)
                    selectedWaypoint = null; // double de/selected node
                else
                    selectedWaypoint = (Waypoint) touchedNode;
            }
        }
    }

    private void setSelectedPOI(Node node) {
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

    public void draw(Canvas canvas) {
        flyPlan.draw(canvas);
    }

    /**
     * Determines touched node
     * @param touchCoordinate
     * @return {@link Node} touched node or null if no node has been touched
     */
    public Node getTouchedNode(Coordinate touchCoordinate) {
        Node touched = null;
        Node node;
        ListIterator iterator;

        // find waypoints
        iterator = flyPlan.getPoints().getWaypoints().listIterator();
        while (iterator.hasNext()) {
            node = (Waypoint) iterator.next();
            if ((node.getShape().getCoordinate().getX() - touchCoordinate.getX()) *
                    (node.getShape().getCoordinate().getX() - touchCoordinate.getX()) +
                    (node.getShape().getCoordinate().getY() - touchCoordinate.getY()) *
                            (node.getShape().getCoordinate().getY() - touchCoordinate.getY()) <=
                    CShape.WAYPOINT_CIRCLE_RADIUS * CShape.WAYPOINT_CIRCLE_RADIUS) {
                touched = node;
                break;
            }
        }

        // find pointOfInterest
        if(touched == null) {
            for (PointOfInterest poi : flyPlan.getPoints().getPointOfInterests()) {
                if ((poi.getShape().getCoordinate().getX() - touchCoordinate.getX()) *
                        (poi.getShape().getCoordinate().getX() - touchCoordinate.getX()) +
                        (poi.getShape().getCoordinate().getY() - touchCoordinate.getY()) *
                                (poi.getShape().getCoordinate().getY() - touchCoordinate.getY()) <=
                        CShape.POI_CIRCLE_RADIUS * CShape.POI_CIRCLE_RADIUS) {
                    touched = poi;
                    break;
                }
            }
        }
        if(touched != null)
            touched.getShape().setCoordinate(touchCoordinate);
        setSelectedNode(touched);
        return touched;
    }

    public float getScaleFactor() {
        return FlyPlanView.getScaleFactor();
    }

    @Override
    public FlyPlan getFlyPlan() {
        return flyPlan;
    }

    @Override
    public FlyPlan onPlanCreateNew() {
        Coordinate coordinate = new Coordinate(0, 0);
        Size size = new Size(50, 50);
        Map map = new Map(coordinate, size);
        return new FlyPlan(map);
    }

    @Override
    public FlyPlan onPlanLoad(File file) {
        try {
            return FlyPlan.loadFromJsonFile(FileExtension.readFile(file));
        } catch (Exception e) {
            e.printStackTrace();
        }   return null;
    }

    @Override
    public boolean onPlanSave() {
        String filenameWithExtension = flyPlan.getTitle() + CFiles.SAVE_DATATYPE;
        String absoluteFlyPlanFilePath = FileExtension.FilePathCombine(CFileDirectories.FLYPLAN_ABSOLUTE, filenameWithExtension);
        return FileExtension.writeToFile(absoluteFlyPlanFilePath, flyPlan.saveToJsonFile());
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