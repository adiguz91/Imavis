package com.drone.imavis.mvp.data.model;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;

/**
 * Created by adigu on 09.08.2017.
 */

/*@Entity(active = false,
        createInDb = false,
        generateConstructors = false,
        generateGettersSetters = false)*/
public class PointOfInterest {

    //@-Id(autoincrement = true)
    private Long id;
    //@-NotNull
    private Long gpsCoordinateId;
    //@ToOne(joinProperty = "gpsCoordinateId")
    private GPSCoordinate gpsCoordinate;
    //@-NotNull
    private Long pointOfInterestDataId;
    //@-ToOne(joinProperty = "pointOfInterestDataId")
    private PointOfInterestData pointOfInterestData;

    public PointOfInterest(GPSCoordinate gpsCoordinate, PointOfInterestData pointOfInterestData) {
        this.gpsCoordinate = gpsCoordinate;
        this.pointOfInterestData = pointOfInterestData;
    }

    public PointOfInterest() {
    }

    public Long getGpsCoordinateId() {
        return gpsCoordinateId;
    }

    public void setGpsCoordinateId(Long gpsCoordinateId) {
        this.gpsCoordinateId = gpsCoordinateId;
    }

    public Long getPointOfInterestDataId() {
        return pointOfInterestDataId;
    }

    public void setPointOfInterestDataId(Long pointOfInterestDataId) {
        this.pointOfInterestDataId = pointOfInterestDataId;
    }

    public void setId(Long id) {
        this.id = id;
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
