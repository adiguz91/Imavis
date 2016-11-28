package com.drone.imavis.draw;

import com.drone.imavis.flyplan.points.NodeItem;

/**
 * Created by Adrian on 26.11.2016.
 */

public interface IMapDraw {

    /**
     * Long touch on map
     * @param point POI or Node
     * @return
     */
    public boolean addNextPoint(Point point);

    /**
     * On touched line click add
     * @param newPoint
     * @param nodeIndex IntEnum: first= -1, last=- 2
     * @return
     */
    public boolean addAfterNode (NodeItem newPoint, int nodeIndex);

    /**
     * Long touch on point and click remove
     * @param point
     * @return
     */
    public boolean remove (Point point);

    /**
     * Long touch on point and click remove
     * @param point
     * @return
     */
    public boolean move (Point point);

}
