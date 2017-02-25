package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.poi;

import android.graphics.Canvas;

import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric.GeometricShape;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.simple.Text;

/**
 * Created by adigu on 03.02.2017.
 */

public class PointOfInterest extends Node implements IPointOfInterestDraw {
    public PointOfInterest(GeometricShape shape, PointOfInterestData data) {
        super(shape, data);
    }

    @Override
    public void addText(Canvas canvas, String content) {
        Text text = new Text<PointOfInterest>(PointOfInterest.class, getShape().getCoordinate(), content);
        text.draw(canvas);
    }
}
