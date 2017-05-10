package com.drone.imavis.mvp.data.model;

/**
 * Created by adigu on 07.05.2017.
 */

public enum TaskPendingAction {
    CANCEL(1),
    REMOVE(2),
    RESTART(3);
    private final int action;

    TaskPendingAction(int action) {
        this.action = action;
    }

    public int getAction() {
        return this.action;
    }
}
