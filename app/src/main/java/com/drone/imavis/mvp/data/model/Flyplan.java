package com.drone.imavis.mvp.data.model;

import android.media.Image;

import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.FlyPlan;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Nodes;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.WaypointData;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoints;
import com.drone.imavis.mvp.util.constants.classes.CFlyPlan;

import java.util.List;

/**
 * Created by adigu on 17.05.2017.
 */

public class Flyplan {

    private String name;
    private int flyHeightMin = CFlyPlan.MIN_FLY_HEIGHT;
    private int flySpeedMin = CFlyPlan.MIN_SPEED;
    private Nodes nodes;
    private Task task;
    //private List<Image> imageList; // or save in task the image collection

    public Flyplan() {}

    public String getName() {
        if(task != null)
            name = task.getName();
        return name;
    }

    public void setName(String name) {
        if(task != null)
            task.setName(name);
        this.name = name;
    }

    public int getFlyHeightMin() {
        return flyHeightMin;
    }

    public void setFlyHeightMin(int flyHeightMin) {
        this.flyHeightMin = flyHeightMin;
    }

    public int getFlySpeedMin() {
        return flySpeedMin;
    }

    public void setFlySpeedMin(int flySpeedMin) {
        this.flySpeedMin = flySpeedMin;
    }

    public Nodes getNodes() {
        return nodes;
    }

    public void setNodes(Nodes nodes) {
        this.nodes = nodes;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
