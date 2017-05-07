package com.drone.imavis.data.model;

import java.util.Date;
import java.util.List;

/**
 * Created by adigu on 06.05.2017.
 */

public class Project {

    private int id;
    private List<Integer> tasks;
    private Date createdAt;
    private String name;
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
