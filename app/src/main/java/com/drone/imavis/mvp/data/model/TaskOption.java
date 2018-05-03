package com.drone.imavis.mvp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adigu on 07.05.2017.
 */

public class TaskOption implements Parcelable {

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
    private String name;
    private String value;

    /* PARCELABLE PART */

    public TaskOption() {
    }

    /**
     * Constructs a Project from a Parcel
     *
     * @param parcelIn Source Parcel
     */
    public TaskOption(Parcel parcelIn) {
        this.name = parcelIn.readString();
        this.value = parcelIn.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
    }
}
