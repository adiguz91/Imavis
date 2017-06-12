package com.drone.flyplanner.data.model.flyplan;

import android.graphics.Canvas;

import com.drone.flyplanner.data.model.flyplan.map.Map;
import com.drone.flyplanner.data.model.flyplan.nodes.Nodes;
import com.drone.flyplanner.data.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.flyplanner.data.model.flyplan.nodes.types.waypoint.Waypoint;

import com.drone.flyplanner.util.constants.classes.CFlyPlan;

import com.drone.flyplanner.util.flyplan.control.IFlyPlanUtil;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by Adrian on 26.11.2016.
 */

public class FlyPlan {

    @Inject
    IFlyPlanUtil flyPlanUtil;

    public FlyPlan(Map map) {
        this.map = map;
        this.nodes = new Nodes();
    }

    public FlyPlan(String jsonFile) {
        this.map = map;
        this.nodes = new Nodes();
    }

    public void draw(Canvas canvas) {
        int selectedWaypointIndex = this.getPoints().getWaypoints().draw(canvas);
        int selectedPoiIndex = this.getPoints().getPointOfInterests().draw(canvas);
        int selectedWaypointId = selectedWaypointIndex + 1;
        int selectedPoiId = selectedPoiIndex + 1;
        Waypoint selectedWaypoint = flyPlanUtil.getSelectedWaypoint();
        PointOfInterest selectedPOI = flyPlanUtil.getSelectedPOI();

        // draw selectedWaypoint
        if(selectedWaypoint != null) {
            selectedWaypoint.setShapeSelectedPaint();
            selectedWaypoint.draw(canvas, String.valueOf(selectedWaypointId), selectedWaypointId);
        }
        // draw selectedPOI
        if(selectedPOI != null) {
            selectedPOI.setShapeSelectedPaint();
            selectedPOI.getShape().setBackgroundColor(this.getPoints().getPointOfInterests().getPoiColorById(selectedPoiIndex));
            selectedPOI.draw(canvas, String.valueOf(selectedPoiId));
        }
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

    public CFlyPlan.UnitOfLength getUnitOfLength() {
        return unitOfLength;
    }
    public void setUnitOfLength(CFlyPlan.UnitOfLength unitOfLength) {
        this.unitOfLength = unitOfLength;
        // todo convert all unitOfLength values and reload
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    private Map map;
    private Nodes nodes;
    private int minFlyHeight = CFlyPlan.MIN_FLY_HEIGHT;
    private int minSpeed = CFlyPlan.MIN_SPEED;
    private CFlyPlan.UnitOfLength unitOfLength = CFlyPlan.UNIT_OF_LENGTH;
    private String title;
}
