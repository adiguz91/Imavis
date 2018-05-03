package com.drone.imavis.mvp.data.model;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;

/**
 * Created by adigu on 09.08.2017.
 */

/*@Entity(active = false,
        createInDb = false,
        generateConstructors = false,
        generateGettersSetters = false)*/
public class Waypoint {

    //@-Id(autoincrement = true)
    private Long id;
    //@-NotNull
    private Long gpsCoordinateId;
    //@-ToOne(joinProperty = "gpsCoordinateId")
    private GPSCoordinate gpsCoordinate;
    //@-NotNull
    private Long waypointDataId;
    //@-ToOne(joinProperty = "waypointDataId")
    private WaypointData waypointData;

    public Waypoint(GPSCoordinate gpsCoordinate, WaypointData waypointData) {
        this.gpsCoordinate = gpsCoordinate;
        this.waypointData = waypointData;
    }

    public Waypoint() {
    }

    public Long getWaypointDataId() {
        return waypointDataId;
    }

    public void setWaypointDataId(Long waypointDataId) {
        this.waypointDataId = waypointDataId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGpsCoordinateId() {
        return this.gpsCoordinateId;
    }

    public void setGpsCoordinateId(Long gpsCoordinateId) {
        this.gpsCoordinateId = gpsCoordinateId;
    }

    //@Keep
    public GPSCoordinate getGpsCoordinate() {
        return gpsCoordinate;
    }

    //@Keep
    public void setGpsCoordinate(GPSCoordinate gpsCoordinate) {
        this.gpsCoordinate = gpsCoordinate;
    }

}
