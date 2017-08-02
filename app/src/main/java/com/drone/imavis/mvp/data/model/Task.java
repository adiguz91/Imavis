package com.drone.imavis.mvp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.FlyPlan;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.map.Map;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Nodes;
import com.drone.imavis.mvp.util.constants.classes.CFlyPlan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by adigu on 06.05.2017.
 */

public class Task implements Parcelable {

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

    @SerializedName("id")
    private int  id;
    @SerializedName("project")
    private int  project;
    @SerializedName("processing_node")
    private int processingNode;
    @SerializedName("images_count")
    private int imagesCount;
    @Expose(serialize = false, deserialize = false)
    private List<String> images; // url
    @SerializedName("available_assets")
    private List<String> availableAssets;
    @SerializedName("uuid")
    private UUID uuid;
    @SerializedName("name")
    private String name;
    @SerializedName("processing_time")
    private int processingTime; // milliseconds
    @SerializedName("auto_processing_node")
    private boolean autoProcessingNode;
    @SerializedName("status")
    private TaskStatus status;
    @SerializedName("last_error")
    private String lastError;
    @SerializedName("options")
    private List<TaskOption> options;
    @SerializedName("ground_control_points")
    private String groundControlPoints;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("pending_action")
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

    public List<TaskOption> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOption> options) {
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

    /* PARCELABLE PART */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(project);
        dest.writeInt(processingNode);
        dest.writeInt(imagesCount);
        dest.writeList(images);
        dest.writeList(availableAssets);
        dest.writeSerializable(uuid);
        dest.writeString(name);
        dest.writeInt(processingTime);
        dest.writeByte((byte) (autoProcessingNode ? 1 : 0));
        dest.writeInt(status == null ? -1 : status.ordinal());
        dest.writeString(lastError);
        dest.writeTypedList(options);
        dest.writeString(groundControlPoints);
        dest.writeLong(createdAt.getTime());
        dest.writeInt(pendingAction == null ? -1 : pendingAction.ordinal());
    }

    /**
     * Constructs a Project from a Parcel
     * @param parcelIn Source Parcel
     */
    public Task (Parcel parcelIn) {
        this.id = parcelIn.readInt();
        this.project = parcelIn.readInt();
        this.processingNode = parcelIn.readInt();
        this.imagesCount = parcelIn.readInt();
        this.images = parcelIn.readArrayList(null);
        this.availableAssets =  parcelIn.readArrayList(null);
        this.uuid = (UUID) parcelIn.readSerializable();
        this.name = parcelIn.readString();
        this.processingTime = parcelIn.readInt();
        this.autoProcessingNode = parcelIn.readByte() != 0;
        this.status = TaskStatus.values()[parcelIn.readInt()];
        this.lastError = parcelIn.readString();
        options = new ArrayList<TaskOption>(); parcelIn.readTypedList(options, TaskOption.CREATOR);
        this.groundControlPoints = parcelIn.readString();
        this.createdAt = new Date((parcelIn.readLong()));
        this.pendingAction = TaskPendingAction.values()[parcelIn.readInt()];
    }

    // Method to recreate a Question from a Parcel
    public static Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {

        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }

    };
}

