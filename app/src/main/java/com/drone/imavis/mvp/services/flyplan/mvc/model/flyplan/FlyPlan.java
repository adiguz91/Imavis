package com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan;

import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;

import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Task;
import com.drone.imavis.mvp.util.constants.classes.CFlyPlan;
import com.drone.imavis.mvp.util.constants.classes.CFlyPlan.UnitOfLength;
import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.map.Map;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Nodes;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Adrian on 26.11.2016.
 */

public class FlyPlan implements Parcelable {

    private Map map;
    private Nodes nodes;
    private int minFlyHeight = CFlyPlan.MIN_FLY_HEIGHT;
    private int minSpeed = CFlyPlan.MIN_SPEED;
    private UnitOfLength unitOfLength = CFlyPlan.UNIT_OF_LENGTH;
    private String title;
    private Task task;

    public FlyPlan(Map map) {
        this.map = map;
        this.nodes = new Nodes();
    }

    public FlyPlan(String jsonFile) {
        this.map = map;
        this.nodes = new Nodes();
    }

    public FlyPlan(Task task) {
        this.map = map;
        this.nodes = new Nodes();
        this.task = task;
    }

    public void draw(Canvas canvas) {
        int selectedWaypointIndex = this.getPoints().getWaypoints().draw(canvas);
        int selectedPoiIndex = this.getPoints().getPointOfInterests().draw(canvas);
        int selectedWaypointId = selectedWaypointIndex + 1;
        int selectedPoiId = selectedPoiIndex + 1;
        Waypoint selectedWaypoint = FlyPlanController.getSelectedWaypoint();
        PointOfInterest selectedPOI = FlyPlanController.getSelectedPOI();

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

    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }

    /* PARCELABLE PART */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(map);
        dest.writeSerializable(nodes);
        dest.writeInt(minFlyHeight);
        dest.writeInt(minSpeed);
        dest.writeString(unitOfLength.name());
        dest.writeString(title);
        dest.writeParcelable(task, flags);
    }

    /**
     * Constructs a Project from a Parcel
     * @param parcelIn Source Parcel
     */
    public FlyPlan (Parcel parcelIn) {
        this.map = (Map) parcelIn.readSerializable();
        this.nodes = (Nodes) parcelIn.readSerializable();
        this.minFlyHeight = parcelIn.readInt();
        this.minSpeed = parcelIn.readInt();
        this.unitOfLength = UnitOfLength.valueOf(parcelIn.readString());
        this.title = parcelIn.readString();
        this.task = parcelIn.readParcelable(Task.class.getClassLoader());
    }

    // Method to recreate a Question from a Parcel
    public static Parcelable.Creator<FlyPlan> CREATOR = new Parcelable.Creator<FlyPlan>() {

        @Override
        public FlyPlan createFromParcel(Parcel source) {
            return new FlyPlan(source);
        }

        @Override
        public FlyPlan[] newArray(int size) {
            return new FlyPlan[size];
        }

    };
}
