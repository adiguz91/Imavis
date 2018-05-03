package com.drone.imavis.mvp.data.model;

import java.util.List;

/**
 * Created by adigu on 09.08.2017.
 */

//@Entity
public class PointOfInterestData {

    //@Id(autoincrement = true)
    private Long id;
    //@NotNull
    private int flyHeight;
    //@ToMany(referencedJoinProperty = "id")
    private List<Waypoint> waypoints;

    public PointOfInterestData() {
    }

    //@Generated(hash = 1598951641)
    public PointOfInterestData(Long id, int flyHeight) {
        this.id = id;
        this.flyHeight = flyHeight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFlyHeight() {
        return flyHeight;
    }

    public void setFlyHeight(int flyHeight) {
        this.flyHeight = flyHeight;
    }

}
