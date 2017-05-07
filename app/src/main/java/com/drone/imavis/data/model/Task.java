package com.drone.imavis.data.model;

import org.json.JSONArray;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by adigu on 06.05.2017.
 */

public class Task {

//    {
//            "id": 134,
//            "project": 27,
//            "processing_node": 10,
//            "images_count": 48,
//            "available_assets": [
//                "all",
//                "geotiff",
//                "texturedmodel",
//                "las",
//                "csv",
//                "ply"
//            ],
//            "uuid": "4338d684-91b4-49a2-b907-8ba171894393",
//            "name": "Task Name",
//            "processing_time": 2197417,
//            "auto_processing_node": false,
//            "status": 40,
//            "last_error": null,
//            "options": [
//                {
//                    "name": "use-opensfm-pointcloud",
//                    "value": true
//                }
//            ],
//            "ground_control_points": null,
//            "created_at": "2017-02-18T18:01:55.402551Z",
//            "pending_action": null
//    }

    private int  id;
    private int  project;
    private int processingNode;
    private int imagesCount;
    private List<String> images; // url
    private List<String> availableAssets;
    private UUID uuid;
    private String name;
    private int processingTime; // milliseconds
    private boolean autoProcessingNode;
    private TaskStatus status;
    private String lastError;
    private JSONArray options;
    private String groundControlPoints;
    private Date createdAt;
    private TaskPendingAction pendingAction;

    public Task() {
        // http://stackoverflow.com/questions/34562950/post-multipart-form-data-using-retrofit-2-0-including-image
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getProject() {
        return project;
    }

    private void setProject(int project) {
        this.project = project;
    }

    public int getProcessingNode() {
        return processingNode;
    }

    public void setProcessingNode(int processingNode) {
        this.processingNode = processingNode;
    }

    public int getImagesCount() {
        return imagesCount;
    }

    private void setImagesCount(int imagesCount) {
        this.imagesCount = imagesCount;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getAvailableAssets() {
        return availableAssets;
    }

    public void setAvailableAssets(List<String> availableAssets) {
        this.availableAssets = availableAssets;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    public boolean isAutoProcessingNode() {
        return autoProcessingNode;
    }

    public void setAutoProcessingNode(boolean autoProcessingNode) {
        this.autoProcessingNode = autoProcessingNode;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public JSONArray getOptions() {
        return options;
    }

    public void setOptions(JSONArray options) {
        this.options = options;
    }

    public String getGroundControlPoints() {
        return groundControlPoints;
    }

    public void setGroundControlPoints(String groundControlPoints) {
        this.groundControlPoints = groundControlPoints;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public TaskPendingAction getPendingAction() {
        return pendingAction;
    }

    public void setPendingAction(TaskPendingAction pendingAction) {
        this.pendingAction = pendingAction;
    }
}

