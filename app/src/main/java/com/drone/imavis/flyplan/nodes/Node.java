package com.drone.imavis.flyplan.nodes;

import com.drone.imavis.flyplan.nodes.shapes.geometric.GeometricShape;

/**
 * Created by adigu on 03.02.2017.
 */

public abstract class Node {

    public Node(GeometricShape shape, NodeData data) {
        this.shape = shape;
        this.data = data;
    }

    public NodeData getData() {
        return data;
    }

    public GeometricShape getShape() {
        return shape;
    }

    private NodeData data;
    private GeometricShape shape;
}
