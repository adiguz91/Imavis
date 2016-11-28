package com.drone.imavis.draw;

import com.drone.imavis.flyplan.points.NodeItem;

/**
 * Created by Adrian on 26.11.2016.
 */

public class MapDraw implements IMapDraw {
    @Override
    public boolean addNextPoint(Point point) {
        return false;
    }

    @Override
    public boolean addAfterNode(NodeItem newPoint, int nodeIndex) {
        return false;
    }

    @Override
    public boolean remove(Point point) {
        return false;
    }

    @Override
    public boolean move(Point point) {
        return false;
    }
}
