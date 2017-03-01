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
import com.drone.imavis.services.flyplan.mvc.model.flyplan.FlyPlan;
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
        //Coordinate idCoordinate = new Coordinate(
        //                            getShape().getCoordinate().getX() + CShape.WAYPOINT_CIRCLE_ID_DISTANCE,
        //                            getShape().getCoordinate().getY() - CShape.WAYPOINT_CIRCLE_ID_DISTANCE);

        int angleDirection = 45;
        float distance = ((Circle)this.getShape()).getRadius() - 10; //  - CShape.WAYPOINT_CIRCLE_ID_DISTANCE
        //Coordinate idCoordinate = FlyPlanMath.getInstance().pointOnCircle(
        //                            getShape().getCoordinate(), distance, angleDirection);
        Coordinate scaled = this.getShape().getCoordinate().toScaleFactor(FlyPlanController.getInstance().getScaleFactor());
        Coordinate coordinateOnCircly = FlyPlanMath.getInstance().
                                            pointOnCircle(scaled, distance, 360 - 45);

        Circle idCircle = new Circle(Integer.class, coordinateOnCircly, CShape.WAYPOINT_CIRCLE_ID_RADIUS);
        idCircle.setBackgroundColor(this.getShape().getBorderColor());
        //idCircle.setBorderColor(Color.parseColor(CColor.WAYPOINT_CIRCLE));
        //idCircle.setBorder(CShape.WAYPOINT_CIRCLE_ID_BORDERSIZE);
        idCircle.draw(canvas, false);

        Text<Waypoint> idText = new Text(Integer.TYPE, coordinateOnCircly, String.valueOf(id));
        idText.setTextColor(Color.WHITE);
        idText.setTextSize(25);
        idText.draw(canvas, false);
    }

    @Override
    public void addLine(Canvas canvas, Waypoint lastWaypoint, Waypoint currentWaypoint) {
        Line line = new Line(lastWaypoint.getShape().getCoordinate(), currentWaypoint.getShape().getCoordinate()) ;
        line.draw(canvas);
    }

    public void addLineWithProgressCircles(Canvas canvas, Waypoint lastWaypoint, Waypoint currentWaypoint) {
        Line line = new Line(lastWaypoint.getShape().getCoordinate(), currentWaypoint.getShape().getCoordinate()) ;
        line.draw(canvas);
    }

    public void drawProgressiveCircles(Canvas canvas, GeometricShape current, GeometricShape next) {
        float numberOfProgressiveCircles = 2;
        float angleOfNextPoint = FlyPlanMath.getInstance().angleBetweenPoints(current, next);
        Coordinate pointOnCircle1 = FlyPlanMath.getInstance().pointOnCircle(current.getCoordinate(), ((Circle)current).getRadius(), angleOfNextPoint);

        float distanceOfTwoPoints = FlyPlanMath.getInstance().distanceOfTwoPoints(pointOnCircle1, next.getCoordinate());
        distanceOfTwoPoints = distanceOfTwoPoints - ((Circle)next).getRadius();
        float firstProgressiveCircleDistance = distanceOfTwoPoints / (numberOfProgressiveCircles + 1);
        //float secondProgressiveCircleDistance = firstProgressiveCircleDistance * 2;

        PointOfInterest poi = ((WaypointData)this.getData()).getPoi();
        if(poi != null)
            next = poi.getShape();

        int radius = 0;
        Coordinate progressivePoint;
        for (int i = 0; i < numberOfProgressiveCircles; i++) {
            radius += firstProgressiveCircleDistance;
            progressivePoint = FlyPlanMath.getInstance().pointOnCircle(pointOnCircle1, radius, angleOfNextPoint);
            Circle progressiveCircle = new Circle(int.class, progressivePoint, 20);
            progressiveCircle.setBackgroundColor(current.getBorderColor());
            progressiveCircle.draw(canvas);
            FlyPlanMath.getInstance().addDirection(canvas,progressiveCircle, next, CShape.WAYPOINT_CIRCLE_ID_RADIUS+6);
        }
    }

    @Override
    public void addDirection(Canvas canvas, Waypoint currentWaypoint, Node nextWaypoint) {
        FlyPlanMath.getInstance().addDirection(canvas, currentWaypoint.getShape(), nextWaypoint.getShape(), CShape.WAYPOINT_CIRCLE_RADIUS);
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
        addIdShape(canvas, id);
    }

}
