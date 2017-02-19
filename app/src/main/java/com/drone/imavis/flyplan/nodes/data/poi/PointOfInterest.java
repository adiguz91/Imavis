package com.drone.imavis.flyplan.nodes.data.poi;

import com.drone.imavis.flyplan.nodes.Node;
import com.drone.imavis.flyplan.nodes.shapes.geometric.GeometricShape;

/**
 * Created by adigu on 03.02.2017.
 */

public class PointOfInterest extends Node {
    public PointOfInterest(GeometricShape shape, PointOfInterestData data) {
        super(shape, data);
    }
}
