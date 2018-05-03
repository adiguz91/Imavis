package com.drone.imavis.mvp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adigu on 06.05.2017.
 */

public class Projects {

    @SerializedName("count")
    private int count;
    @SerializedName("next")
    private String next;
    @SerializedName("previous")
    private String previous;
    @SerializedName("results")
    private List<Project> projectList;

    public Projects() {
    }

    public Projects(List<Project> projectList) {
        this.projectList = projectList;
    }

    public int getCount() {
        return count;
    }

    private void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    private void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    private void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}
