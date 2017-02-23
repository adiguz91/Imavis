package com.drone.imavis.flyplan.mvc.model.flyplan;

import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.constants.classes.CFlyPlan.UnitOfLength;
import com.drone.imavis.flyplan.mvc.model.flyplan.map.Map;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Nodes;
import com.google.gson.Gson;

/**
 * Created by Adrian on 26.11.2016.
 */

public class FlyPlan {

    public FlyPlan(Map map) {
        this.map = map;
        this.nodes = new Nodes();
    }

    public FlyPlan(String jsonFile) {
        this.map = map;
        this.nodes = new Nodes();
    }

    public static FlyPlan loadFromJsonFile(String jsonFileContent) {
        return new Gson().fromJson(jsonFileContent, FlyPlan.class);
    }

    public String saveToJsonFile() {
        return new Gson().toJson(this);
    }

    public Map getMap() {
        return map;
    }

    private void setMap(Map map) {
        this.map = map;
    }

    public Nodes getPoints() {
        return nodes;
    }

    private void setPoints(Nodes points) {
        this.nodes = nodes;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    Map map;
    Nodes nodes;
    int minFlyHeight = CFlyPlan.MIN_FLY_HEIGHT;
    int minSpeed = CFlyPlan.MIN_SPEED;
    UnitOfLength unitOfLength = CFlyPlan.UNIT_OF_LENGTH;
    private String title;
}
