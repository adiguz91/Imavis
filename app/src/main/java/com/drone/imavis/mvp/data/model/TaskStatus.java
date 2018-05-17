package com.drone.imavis.mvp.data.model;


import com.drone.imavis.mvp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adigu on 07.05.2017.
 */

public enum TaskStatus {
    UNTOUCHED(0, R.color.md_black_1000), // BLACK
    QUEUED(10, R.color.md_orange_500), // ORANGE
    RUNNING(20, R.color.md_orange_500), // ORANGE
    FAILED(30, R.color.md_red_500), // RED
    COMPLETED(40, R.color.md_green_500), // green
    CANCELED(50, R.color.md_grey_500); // GREY

    private final int code;
    private static Map map = new HashMap();

    static {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            map.put(taskStatus.code, taskStatus);
        }
    }

    private final int ressourceColor;

    TaskStatus(int code, int ressourceColor) {
        this.code = code;
        this.ressourceColor = ressourceColor;
    }

    public static TaskStatus valueOf(int code) {
        return (TaskStatus) map.get(code);
    }

    public int getCode() {
        return this.code;
    }

    public int getRessourceColor() {
        return this.ressourceColor;
    }
}
