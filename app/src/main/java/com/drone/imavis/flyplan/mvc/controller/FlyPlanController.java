package com.drone.imavis.flyplan.mvc.controller;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.drone.imavis.constants.classes.CFileDirectories;
import com.drone.imavis.constants.classes.CFiles;
import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.flyplan.mvc.model.flyplan.FlyPlan;
import com.drone.imavis.flyplan.mvc.model.flyplan.map.Map;
import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.model.extensions.dimension.Size;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.WaypointData;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Circle;
import com.drone.imavis.flyplan.mvc.view.flyplan.FlyPlanView;
import com.google.android.gms.maps.GoogleMap;

import java.io.File;
import java.util.LinkedHashMap;

import extensions.file.FileExtension;

/**
 * Created by adigu on 23.02.2017.
 */
public class FlyPlanController implements IFlyPlan {

    private java.util.Map<Integer, Node> nodes = new LinkedHashMap<>();
    private SparseArray<Node> nodePointers = new SparseArray(CFlyPlan.MAX_NODES);

    private static FlyPlanController flyPlanController;
    private FlyPlan flyPlan;

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

    public void init(final Context context) {
    }


    /**
     * Search and creates new (if needed) node based on touch area
     *
     * @param touchCoordinate
     *
     * @return obtained {@link Node}
     */
    public Node obtainTouchedNode(Coordinate touchCoordinate) {
        Node touchedNode = getTouchedNode(touchCoordinate);

        if (touchedNode == null) {
            Circle shape = new Circle(touchCoordinate, CShape.WAYPOINT_CIRCLE_SIZE/2);
            WaypointData nodeData = new WaypointData();
            touchedNode = new Waypoint(shape, nodeData);
            touchedNode.getShape().setCoordinate(touchCoordinate);

            if (nodes.size() == CFlyPlan.MAX_NODES) {
                //Log.w(TAG, "Clear all circles, size is " + nodes.size());
                // remove first node
                nodes.clear();
            }
            //Log.w(TAG, "Added node " + touchedNode);
            nodes.put(nodes.size(), touchedNode);
        }

        touchedNode.getShape().setCoordinate(touchCoordinate);
        return touchedNode;
    }

    /**
     * Determines touched node
     *
     * @param touchCoordinate
     *
     * @return {@link Node} touched node or null if no node has been touched
     */
    private Node getTouchedNode(Coordinate touchCoordinate) {
        Node touched = null;
        Node node;

        int nodeSize = CShape.WAYPOINT_CIRCLE_SIZE;

        for (java.util.Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            node = entry.getValue();
            if ((node.getShape().getCoordinate().getX() - touchCoordinate.getX()) *
                    (node.getShape().getCoordinate().getX() - touchCoordinate.getX()) +
                    (node.getShape().getCoordinate().getY() - touchCoordinate.getY()) *
                            (node.getShape().getCoordinate().getY() - touchCoordinate.getY()) <=
                    nodeSize * nodeSize) {
                touched = node;
                break;
            }
        }

        if(touched != null)
            touched.getShape().setCoordinate(touchCoordinate);
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
        return null;
    }



    @Override
    public FlyPlan onPlanLoad(File file) {
        try
        {
            return FlyPlan.loadFromJsonFile(FileExtension.readFile(file));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
