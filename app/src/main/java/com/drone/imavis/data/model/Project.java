package com.drone.imavis.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by adigu on 06.05.2017.
 */

public class Project {

    @SerializedName("id")
    private int id;
    @SerializedName("tasks")
    private List<Integer> tasks;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;

    public Project() {}

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public List<Integer> getTasks() {
        return tasks;
    }

    private void setTasks(List<Integer> tasks) {
        this.tasks = tasks;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    private void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
