package com.drone.imavis.mvp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by adigu on 07.05.2017.
 */

public class TaskOption implements Parcelable {

    private String name;
    private String value;

    public TaskOption() {}

    /* PARCELABLE PART */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
    }

    /**
     * Constructs a Project from a Parcel
     * @param parcelIn Source Parcel
     */
    public TaskOption (Parcel parcelIn) {
        this.name = parcelIn.readString();
        this.value = parcelIn.readString();
    }

    // Method to recreate a Question from a Parcel
    public static Parcelable.Creator<TaskOption> CREATOR = new Parcelable.Creator<TaskOption>() {

        @Override
        public TaskOption createFromParcel(Parcel source) {
            return new TaskOption(source);
        }

        @Override
        public TaskOption[] newArray(int size) {
            return new TaskOption[size];
        }

    };
}
