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

    private static Node touchedNodeGlobal;
    private static PointOfInterest selectedPOI;
    public static Node getTouchedNode() { return touchedNodeGlobal; }
    public static Node getSelectedPOI() { return selectedPOI; }

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
                //touchedNode.getShape().setCoordinate(touchCoordinate);
                if (flyPlan.getPoints().getWaypoints().size() == CFlyPlan.MAX_WAYPOINTS_SIZE) {
                    flyPlan.getPoints().getWaypoints().clear();
                }
            }
            if(className == PointOfInterest.class) {
                //GeometricShape shape = new Circle(PointOfInterest.class, touchCoordinate, CShape.POI_CIRCLE_RADIUS);
                //PointOfInterestData nodeData = new PointOfInterestData();
                touchedNode = new PointOfInterest(touchCoordinate);
                //touchedNode.getShape().setCoordinate(touchCoordinate);
                if (flyPlan.getPoints().getPointOfInterests().size() == CFlyPlan.MAX_POI_SIZE) {
                    flyPlan.getPoints().getPointOfInterests().clear();
                }
            }
            //Log.w(TAG, "Added node " + touchedNode);
            flyPlan.getPoints().addNode(touchedNode);
            setTouchedNodeGlobal(touchedNode);
            return true;
        }
        touchedNode.getShape().setCoordinate(touchCoordinate);
        setTouchedNodeGlobal(touchedNode);
        return false;
    }

    private void setTouchedNodeGlobal(Node touchedNode) {
        //touchedNodeGlobalLast = touchedNodeGlobal;
        boolean isDoubleSelectedPOI = false;

        if(touchedNode == null)
            selectedPOI = null;

        if(getSelectedPOI() == touchedNode) {
            // double toggle node selectedPOI
            selectedPOI = selectedPOI;
        } else {
            if(touchedNode != null && touchedNode.getClass() == PointOfInterest.class)
                selectedPOI = (PointOfInterest) touchedNode;
        }
        if(touchedNodeGlobal == touchedNode)
            touchedNode = null; // double de/selected node
        touchedNodeGlobal = touchedNode;
    }

    public void draw(Canvas canvas) {
        int counter = 1;
        int selectedId = 1;
        Waypoint waypoint, waypointLastNode = null;
        PointOfInterest poi;
        ListIterator<Waypoint> iterator;

        // draw waypoints
        iterator = getFlyPlan().getPoints().getWaypoints().listIterator();
        while (iterator.hasNext()) {
            waypoint = iterator.next();
            if(waypointLastNode != null)
                waypoint.addLine(canvas, waypointLastNode, waypoint);
            if(waypoint != FlyPlanController.getTouchedNode()) {
                waypoint.getShape().draw(canvas);
                waypoint.addText(canvas, String.valueOf(counter));
            } else
                selectedId = counter;

            if(waypointLastNode != null)
                waypoint.addDirection(canvas, waypointLastNode, waypoint);

            waypointLastNode = waypoint;
            counter++;
        }

        // draw POIs
        counter = 1;
        for (PointOfInterest pointOfInterest : getFlyPlan().getPoints().getPointOfInterests()) {
            poi = pointOfInterest;
            if(poi != FlyPlanController.getTouchedNode() ) {
                poi.getShape().draw(canvas);
                poi.addText(canvas, String.valueOf(counter));
            } else
                selectedId = counter;
            counter++;
        }

        // draw selectedNode
        if(FlyPlanController.getTouchedNode() != null) {

            if(FlyPlanController.getTouchedNode().getClass() == Waypoint.class) {
                if(FlyPlanController.getSelectedPOI() != null) {
                    // add waypoint to poi
                    ListIterator listIterator = getFlyPlan().getPoints().getWaypoints().listIterator();
                    int touchedNodeId = 0;
                    while (listIterator.hasNext()) {
                        Node node = (Waypoint) listIterator.next();
                        if(node == getTouchedNode())
                            break;
                        touchedNodeId++;
                    }
                    ((WaypointData) flyPlan.getPoints().getWaypoints().get(touchedNodeId).getData()).
                            setPoi((PointOfInterest) getSelectedPOI());

                    ((Circle) FlyPlanController.getSelectedPOI().getShape()).draw(canvas, true);
                }

            }

            if( FlyPlanController.getTouchedNode().getShape().getClass() == Circle.class) {
                ((Circle) FlyPlanController.getTouchedNode().getShape()).draw(canvas, true);
            } else
                FlyPlanController.getTouchedNode().getShape().draw(canvas);

            if( FlyPlanController.getTouchedNode().getClass() == Waypoint.class)
                ((Waypoint) FlyPlanController.getTouchedNode()).addText(canvas, String.valueOf(selectedId));
            else if( FlyPlanController.getTouchedNode().getClass() == PointOfInterest.class)
                ((PointOfInterest) FlyPlanController.getTouchedNode()).addText(canvas, String.valueOf(selectedId));
        }
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

        if(touched != null) {
            touched.getShape().setCoordinate(touchCoordinate);
        }
        setTouchedNodeGlobal(touched);
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
