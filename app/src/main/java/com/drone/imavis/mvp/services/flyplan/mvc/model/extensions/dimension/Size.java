package com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.dimension;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Size {
    private int width;
    private int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
