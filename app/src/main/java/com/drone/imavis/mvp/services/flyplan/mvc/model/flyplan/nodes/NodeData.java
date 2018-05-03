package com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes;

/**
 * Created by adigu on 03.02.2017.
 */

public abstract class NodeData<T> {

    private Long id;

    public NodeData() {
        id = Long.MIN_VALUE;
    }

    public NodeData(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
