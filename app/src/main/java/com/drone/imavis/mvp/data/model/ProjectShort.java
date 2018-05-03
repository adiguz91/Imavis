package com.drone.imavis.mvp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by adigu on 28.07.2017.
 */

public class ProjectShort {

    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;

    public ProjectShort(String name, String description) {
        this.name = name;
        this.description = description;
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
