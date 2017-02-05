package com.drone.imavis.flyplan;

import com.drone.imavis.flyplan.coordinates.Coordinate;
import com.drone.imavis.flyplan.dimension.Size;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Map {
    Coordinate coordinate;
    Size size;

    public Map(Coordinate coordinate, Size size) {
        this.coordinate = coordinate;
        this.size = size;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
