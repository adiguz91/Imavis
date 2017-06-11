package com.drone.flyplanner.data.model.flyplan.nodes.types.poi;



import com.drone.flyplanner.data.model.flyplan.nodes.NodeData;
import com.drone.flyplanner.data.model.flyplan.nodes.types.waypoint.WaypointData;

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
