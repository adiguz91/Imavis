package com.drone.imavis.mvp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.MultipartBody;

/**
 * Created by adigu on 06.05.2017.
 */

public class Task implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("project")
    private int project;

    @SerializedName("processing_node")
    private int processingNode;

    @SerializedName("images_count")
    private int imagesCount;

    //@Expose(serialize = false, deserialize = false)
    List<MultipartBody.Part> images; // url

    @SerializedName("available_assets")
    private List<String> availableAssets;

    @SerializedName("uuid")
    private String uuid;
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
    //@SerializedName("options")
    //private List<TaskOption> options;
    @SerializedName("ground_control_points")
    private String groundControlPoints;
    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("pending_action")
    private TaskPendingAction pendingAction;

    public Task() {
        // http://stackoverflow.com/questions/34562950/post-multipart-form-data-using-retrofit-2-0-including-image
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
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

    public void setImagesCount(int imagesCount) {
        this.imagesCount = imagesCount;
    }

    public List<MultipartBody.Part> getImages() {
        return images;
    }

    public void setImages(List<MultipartBody.Part> images) {
        this.images = images;
    }

    public List<String> getAvailableAssets() {
        return availableAssets;
    }

    public void setAvailableAssets(List<String> availableAssets) {
        this.availableAssets = availableAssets;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
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

    public String getStatusString() {
         try {
            return status.toString();
        } catch (Exception ex) {
             return "";
         }
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
/*
    public List<TaskOption> getOptions() {
        return options;
    }
    public void setOptions(List<TaskOption> options) {
        this.options = options;
    }
*/
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

    public boolean getAutoProcessingNode() {
        return this.autoProcessingNode;
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
        dest.writeString(id);
        dest.writeInt(project);
        dest.writeInt(processingNode);
        dest.writeInt(imagesCount);
        dest.writeList(images);
        dest.writeList(availableAssets);
        dest.writeString(uuid);
        dest.writeString(name);
        dest.writeInt(processingTime);
        dest.writeByte((byte) (autoProcessingNode ? 1 : 0));
        dest.writeInt(status == null ? -1 : status.ordinal());
        dest.writeString(lastError);
        //dest.writeTypedList(options);
        dest.writeString(groundControlPoints);
        dest.writeLong(createdAt.getTime());
        dest.writeInt(pendingAction == null ? -1 : pendingAction.ordinal());
    }

    /**
     * Constructs a Project from a Parcel
     * @param parcelIn Source Parcel
     */
    public Task (Parcel parcelIn) {
        this.id = parcelIn.readString();
        this.project = parcelIn.readInt();
        this.processingNode = parcelIn.readInt();
        this.imagesCount = parcelIn.readInt();
        this.images = parcelIn.readArrayList(null);
        this.availableAssets =  parcelIn.readArrayList(null);
        this.uuid = parcelIn.readString();
        this.name = parcelIn.readString();
        this.processingTime = parcelIn.readInt();
        this.autoProcessingNode = parcelIn.readByte() != 0;
        int temp = parcelIn.readInt();
        this.status = TaskStatus.values()[temp == -1 ? 0 : temp];
        this.lastError = parcelIn.readString();
        //this.options = null; //new ArrayList<TaskOption>(); parcelIn.readTypedList(this.options, null);
        this.groundControlPoints = parcelIn.readString();
        this.createdAt = new Date((parcelIn.readLong()));
        temp = parcelIn.readInt();
        this.pendingAction = TaskPendingAction.values()[temp == -1 ? 0 : temp];
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

