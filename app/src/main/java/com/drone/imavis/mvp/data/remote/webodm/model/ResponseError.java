package com.drone.imavis.mvp.data.remote.webodm.model;

import com.google.gson.annotations.Expose;

/**
 * Created by adigu on 14.05.2017.
 */

public class ResponseError {

    private Throwable throwable;
    private String code;
    private String description;

    public ResponseError(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
