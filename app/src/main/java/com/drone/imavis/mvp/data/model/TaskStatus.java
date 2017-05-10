package com.drone.imavis.mvp.data.model;

/**
 * Created by adigu on 07.05.2017.
 */

public enum TaskStatus {
    QUEUED(10),
    RUNNING(20),
    FAILED(30),
    COMPLETED(40),
    CANCELED(50);
    private final int code;

    TaskStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
