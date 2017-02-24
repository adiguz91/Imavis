package com.drone.imavis.flyplan.mvc.model.flyplan.nodes;

/**
 * Created by adigu on 03.02.2017.
 */

public abstract class NodeData {

    public NodeData() {}

    public NodeData(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
}
