package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.data.poi;

import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.NodeData;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.data.waypoint.WaypointData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 26.11.2016.
 */

public class PointOfInterestData extends NodeData {

    public PointOfInterestData() {}

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<WaypointData> getNodeItems() {
        if(nodeItems == null)
            nodeItems = new ArrayList<>();
        return nodeItems;
    }

    public void setNodeItems(List<WaypointData> nodeItems) {
        this.nodeItems = nodeItems;
    }

    //int id;
    int height;
    List<WaypointData> nodeItems;
}
