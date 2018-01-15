package com.drone.imavis.mvp.data.model;

import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.WaypointMode;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by adigu on 09.08.2017.
 */

//@Entity
public class WaypointData {

    public WaypointData() {}

    /* generated
    public WaypointData(Long id, Long poiID, int distanceToNextNode, int speedToNextNode, int flyHeight,
            @NotNull WaypointMode mode) {
        this.id = id;
        this.poiID = poiID;
        this.distanceToNextNode = distanceToNextNode;
        this.speedToNextNode = speedToNextNode;
        this.flyHeight = flyHeight;
        this.mode = mode;
    }*/

    //@Id(autoincrement = true)
    private Long id;

    private Long poiID;
    //@ToOne(joinProperty = "poiID")
    private PointOfInterest poi;

    //private int direction;

    //@NotNull
    private int distanceToNextNode;

    //@NotNull
    private int speedToNextNode;

    //@NotNull
    private int flyHeight;

    //@NotNull
    //@Convert(converter = WaypointModeConverter.class, columnType = String.class)
    private WaypointMode mode;

    public int getDistanceToNextNode() {
        return distanceToNextNode;
    }
    public void setDistanceToNextNode(int distanceToNextNode) {
        this.distanceToNextNode = distanceToNextNode;
    }

    public int getSpeedToNextNode() {
        return speedToNextNode;
    }
    public void setSpeedToNextNode(int speedToNextNode) {
        this.speedToNextNode = speedToNextNode;
    }

    public int getFlyHeight() {
        return flyHeight;
    }
    public void setFlyHeight(int flyHeight) {
        this.flyHeight = flyHeight;
    }

    public WaypointMode getMode() {
        return mode;
    }
    public void setMode(WaypointMode mode) {
        this.mode = mode;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getPoiID() {
        return this.poiID;
    }
    public void setPoiID(Long poiID) {
        this.poiID = poiID;
    }
	
	/* GreenDAO CONVERTER */

    static class WaypointModeConverter implements PropertyConverter<WaypointMode, String> {
        @Override
        public WaypointMode convertToEntityProperty(String databaseValue) {
            return WaypointMode.valueOf(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(WaypointMode entityProperty) {
            return entityProperty.name();
        }
    }
    
}
