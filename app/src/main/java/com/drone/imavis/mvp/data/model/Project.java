package com.drone.imavis.mvp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by adigu on 06.05.2017.
 */

public class Project implements Parcelable {

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

    /* PARCELABLE PART */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeList(tasks);
        dest.writeLong(createdAt.getTime());
    }

    /**
     * Constructs a Project from a Parcel
     * @param parcelIn Source Parcel
     */
    public Project (Parcel parcelIn) {
        this.id = parcelIn.readInt();
        this.name = parcelIn.readString();
        this.description = parcelIn.readString();
        this.tasks = parcelIn.readArrayList(null);
        this.createdAt = new Date(parcelIn.readLong());
    }

    // Method to recreate a Question from a Parcel
    public static Creator<Project> CREATOR = new Creator<Project>() {

        @Override
        public Project createFromParcel(Parcel source) {
            return new Project(source);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }

    };
}
