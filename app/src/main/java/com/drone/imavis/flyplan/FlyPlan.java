package com.drone.imavis.flyplan;

import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.constants.classes.CFlyPlan.UnitOfLength;

/**
 * Created by Adrian on 26.11.2016.
 */

public class FlyPlan {
    Map map;
    Points points;
    int minFlyHeight = CFlyPlan.MIN_FLY_HEIGHT;
    int minSpeed = CFlyPlan.MIN_SPEED;
    UnitOfLength unitOfLength = CFlyPlan.UNIT_OF_LENGTH;

    public FlyPlan(Map map) {
        this.map = map;
        points = new Points();
    }

    public Map getMap() {
        return map;
    }

    private void setMap(Map map) {
        this.map = map;
    }

    public Points getPoints() {
        return points;
    }

    private void setPoints(Points points) {
        this.points = points;
    }

    public int getMinFlyHeight() {
        return minFlyHeight;
    }

    public void setMinFlyHeight(int minFlyHeight) {
        this.minFlyHeight = minFlyHeight;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public UnitOfLength getUnitOfLength() {
        return unitOfLength;
    }

    public void setUnitOfLength(UnitOfLength unitOfLength) {
        this.unitOfLength = unitOfLength;
        // todo convert all unitOfLength values and reload
    }
}
