package com.drone.imavis.mvp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.drone.imavis.mvp.util.constants.AppConstants;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;

/**
 * Created by adigu on 06.05.2017.
 */

public class Task implements Parcelable {

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
    //@Expose(serialize = false, deserialize = false)
    List<MultipartBody.Part> images; // url
    @SerializedName("id")
    private String id;
    @SerializedName("project")
    private int project;
    @SerializedName("processing_node")
    private int processingNode;
    @SerializedName("images_count")
    private int imagesCount;
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
    private int status;
    //@Expose(serialize = false, deserialize = false)
    //private TaskStatus taskStatus;
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

    /**
     * Constructs a Project from a Parcel
     *
     * @param parcelIn Source Parcel
     */
    public Task(Parcel parcelIn) {
        this.id = parcelIn.readString();
        this.project = parcelIn.readInt();
        this.processingNode = parcelIn.readInt();
        this.imagesCount = parcelIn.readInt();
        this.images = parcelIn.readArrayList(null);
        this.availableAssets = parcelIn.readArrayList(null);
        this.uuid = parcelIn.readString();
        this.name = parcelIn.readString();
        this.processingTime = parcelIn.readInt();
        this.autoProcessingNode = parcelIn.readByte() != 0;
        this.status = parcelIn.readInt();
        this.lastError = parcelIn.readString();
        //this.options = null; //new ArrayList<TaskOption>(); parcelIn.readTypedList(this.options, null);
        this.groundControlPoints = parcelIn.readString();
        this.createdAt = new Date((parcelIn.readLong()));
        int temp = parcelIn.readInt();
        this.pendingAction = TaskPendingAction.values()[temp == -1 ? 0 : temp];
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

    public String getProcessingTimeString() {
        long second = (processingTime / 1000) % 60;
        long minute = (processingTime / (1000 * 60)) % 60;
        long hour = (processingTime / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    public boolean isAutoProcessingNode() {
        return autoProcessingNode;
    }

    public TaskStatus getTaskStatus() {
        return TaskStatus.valueOf(status);
    }

    public String getStatusString() {
        if (getTaskStatus() == null)
            return TaskStatus.UNTOUCHED.toString();

        try {
            return getTaskStatus().toString();
        } catch (Exception ex) {
            return TaskStatus.UNTOUCHED.toString();
        }
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

    public String getCreatedAtString() {
        if (getCreatedAt() == null)
            return "";

        // TODO: check if englisch or german
        DateFormat dateFormat = new SimpleDateFormat(AppConstants.dateFormatGerman); // english version MM/dd/yyyy HH:mm:ss
        return dateFormat.format(getCreatedAt());
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

    public boolean getAutoProcessingNode() {
        return this.autoProcessingNode;
    }

    public void setAutoProcessingNode(boolean autoProcessingNode) {
        this.autoProcessingNode = autoProcessingNode;
    }

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
        dest.writeInt(status);
        dest.writeString(lastError);
        //dest.writeTypedList(options);
        dest.writeString(groundControlPoints);
        dest.writeLong(createdAt.getTime());
        dest.writeInt(pendingAction == null ? -1 : pendingAction.ordinal());
    }
}

