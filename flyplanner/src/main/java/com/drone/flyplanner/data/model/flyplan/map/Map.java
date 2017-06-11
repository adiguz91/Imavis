package com.drone.flyplanner.data.model.flyplan.map;

import com.drone.flyplanner.util.models.coordinates.Coordinate;
import com.drone.flyplanner.util.models.dimension.Size;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Map<T> {

    private T map;
    private Coordinate coordinate;
    private Size size;

    public Map(Coordinate coordinate, Size size) {
        this.coordinate = coordinate;
        this.size = size;
    }

    public T getMap() { return map; }

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
