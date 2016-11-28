package com.drone.imavis.flyplan.points;

import com.drone.imavis.flyplan.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 26.11.2016.
 */

public class PointOfInterest extends PointItem {
    //int id;
    int height;
    Coordinate coordinate;
    List<NodeItem> nodeItems;

    public PointOfInterest() {}

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public List<NodeItem> getNodeItems() {
        if(nodeItems == null)
            nodeItems = new ArrayList<>();
        return nodeItems;
    }

    public void setNodeItems(List<NodeItem> nodeItems) {
        this.nodeItems = nodeItems;
    }
}
