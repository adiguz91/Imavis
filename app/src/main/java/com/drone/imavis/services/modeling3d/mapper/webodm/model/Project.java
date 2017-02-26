package com.drone.imavis.services.modeling3d.mapper.webodm.model;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adigu on 18.01.2017.
 */

public class Project {

    public Project(long projectId, String projectName, List<Image> imageCollection) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.imageCollection = imageCollection;
    }

    public List<Image> getImageCollection() {
        if(imageCollection == null)
            imageCollection = new ArrayList<>();
        return imageCollection;
    }

    public void setImageCollection(List<Image> imageCollection) {
        this.imageCollection = imageCollection;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getProjectId() {
        return projectId;
    }

    private void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public Status getProjectStatus() {
        return projectStatus;
    }

    protected void setProjectStatus(Status projectStatus) {
        this.projectStatus = projectStatus;
    }

    public enum Status {
        Idle,
        Faild,
        Uploading,
        Finished,
    }

    private List<Image> imageCollection;
    private String projectName;
    private long projectId;
    private Status projectStatus = Status.Idle;
}

