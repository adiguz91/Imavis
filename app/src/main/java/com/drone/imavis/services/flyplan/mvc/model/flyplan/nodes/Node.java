package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes;

import com.drone.imavis.util.constants.classes.CShape;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterestData;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.WaypointData;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Circle;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.GeometricShape;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Square;

/**
 * Created by adigu on 03.02.2017.
 */

public abstract class Node<T> {

    public Node(Class<T> classNode, Coordinate coordinateTouched) {
        this.classT = classNode;
        this.data = createData(classNode);
        this.shape = createGeoShape(classNode, coordinateTouched);
    }

    private NodeData<T> createData(Class classNode) {
        if(classNode == Waypoint.class)
            return new WaypointData();
        else // if (classNode == PointOfInterest.class)
            return new PointOfInterestData();
    }

    private GeometricShape<T> createGeoShape(Class classNode, Coordinate coordinate) {
        if(classNode == Waypoint.class)
            return createShape(CShape.WAYPOINT_SHAPE_TYPE, coordinate);
        else // if (classNode == PointOfInterest.class)
            return createShape(CShape.POI_SHAPE_TYPE, coordinate);
    }

    private GeometricShape createShape(Class classShape, Coordinate coordinate) {
        GeometricShape geometricShape = null;
        if (classShape == Circle.class) {
            if (classT == Waypoint.class)
                geometricShape = new Circle<T>(classT, coordinate, CShape.WAYPOINT_CIRCLE_RADIUS);
            else if (classT == PointOfInterest.class)
                geometricShape = new Circle<T>(classT, coordinate, CShape.POI_CIRCLE_RADIUS);
        }
        if (classShape == Square.class) {
            if (classT == Waypoint.class)
                geometricShape =  new Square<T>(classT, coordinate, CShape.WAYPOINT_CIRCLE_RADIUS);
            else if (classT == PointOfInterest.class)
                geometricShape =  new Square<T>(classT, coordinate, CShape.POI_CIRCLE_RADIUS);
        }
        return geometricShape;
    }

    public NodeData<T> getData() { return data; }
    public GeometricShape<T> getShape() {
        return shape;
    }

    private Class<T> classT;
    private NodeData<T> data;
    private GeometricShape<T> shape;

    /*
    private T getInstance() {
        try {
            return classT.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }   return null;
    }
    */
}
