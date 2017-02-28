package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.extensions.flyplan.math.FlyPlanMath;
import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Circle;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.geometric.GeometricShape;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.simple.Line;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.simple.Text;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;

/**
 * Created by adigu on 03.02.2017.
 */

public class Waypoint<T> extends Node implements IWaypointDraw {

    public Waypoint(Coordinate touchedCoordinate) {
        super(Waypoint.class, touchedCoordinate);
        setShapePaint();
        //this.shape = createShape(CShape.WAYPOINT_SHAPE_TYPE, touchedCoordinate);
    }

    public void setShapePaint() {
        this.getShape().setBackgroundColor(Color.parseColor(CColor.WAYPOINT_CIRCLE));
        this.getShape().setBorderColor(Color.parseColor(CColor.WAYPOINT_CIRCLE_BORDER));
        this.getShape().setBorder(CShape.WAYPOINT_CIRCLE_BORDERSIZE);
    }

    private void setPoiShapePaint(PointOfInterest poi) {
        poi.setShapePaint();
        this.getShape().setBackgroundColor(poi.getShape().getBackgroundColor());
    }

    public void setShapeSelectedPaint() {
        this.getShape().setBackgroundColor(Color.parseColor(CColor.NODE_SELECTED_CIRCLE));
        this.getShape().setBorderColor(Color.parseColor(CColor.NODE_SELECTED_CIRCLE_BORDER));
    }

    public void addText(Canvas canvas, String content) {
        Text<Waypoint> text = new Text<Waypoint>(Waypoint.class, getShape().getCoordinate(), content);
        text.draw(canvas);
    }

    private void addIdShape(Canvas canvas, int id) {
        Coordinate idCoordinate = new Coordinate(
                                    getShape().getCoordinate().getX() + CShape.WAYPOINT_CIRCLE_ID_DISTANCE,
                                    getShape().getCoordinate().getY() - CShape.WAYPOINT_CIRCLE_ID_DISTANCE);

        int angleDirection = 45;
        float distance = ((Circle)this.getShape()).getRadius() - CShape.WAYPOINT_CIRCLE_ID_DISTANCE;
        //Coordinate idCoordinate = FlyPlanMath.getInstance().pointOnCircle(
        //                            getShape().getCoordinate(), distance, angleDirection);

        GeometricShape idCircle = new Circle(Integer.class, idCoordinate, CShape.WAYPOINT_CIRCLE_ID_RADIUS);
        idCircle.setBackgroundColor(Color.parseColor(CColor.WAYPOINT_CIRCLE));
        idCircle.setBorderColor(Color.parseColor(CColor.WAYPOINT_CIRCLE));
        idCircle.setBorder(CShape.WAYPOINT_CIRCLE_ID_BORDERSIZE);
        idCircle.draw(canvas);

        Text<Waypoint> idText = new Text(Integer.TYPE, idCoordinate, String.valueOf(id));
        idText.draw(canvas);
    }

    @Override
    public void addLine(Canvas canvas, Waypoint lastWaypoint, Waypoint currentWaypoint) {
        Line line = new Line(lastWaypoint.getShape().getCoordinate(), currentWaypoint.getShape().getCoordinate()) ;
        line.draw(canvas);
    }

    @Override
    public void addDirection(Canvas canvas, Waypoint currentWaypoint, Node nextWaypoint) {
        FlyPlanMath.getInstance().addDirection(canvas, currentWaypoint, nextWaypoint);
    }

    @Override
    public void draw(Canvas canvas, String content, int id) {
        PointOfInterest poi = ((WaypointData) this.getData()).getPoi();
        if(poi != null)
            this.setPoiShapePaint(poi);
        //else
        //    this.setShapePaint();
        this.getShape().draw(canvas);
        addText(canvas, content);
        //addIdShape(canvas, id);
    }

}
