package com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.map;

import com.drone.imavis.mvp.data.model.MapData;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;

/**
 * Created by Adrian on 26.11.2016.
 */

public abstract class Map<T> {

    private T map;
    private MapData mapData;

    public Map(T map) {
        this.map = map;
    }

    public Map(T map, MapData mapData) {
        this.map = map;
        this.mapData = mapData;
    }

    public T getMap() { return map; }

    public MapData getMapData() {
        return mapData;
    }

    public void setMapData(MapData mapData) {
        this.mapData = mapData;
    }

    public abstract GPSCoordinate getGpsfromScreen(Coordinate coordinate);

    public abstract Coordinate getScreenCoordinatesfromGps(GPSCoordinate gpsCoordinate);
}
