package com.drone.imavis.mvp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by adigu on 28.07.2017.
 */

public class ProjectShort {

    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;

    public ProjectShort() {}

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
