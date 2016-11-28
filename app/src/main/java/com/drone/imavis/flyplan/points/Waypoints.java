package com.drone.imavis.flyplan.points;

import com.google.gson.Gson;
import extensions.doublelinkedlist.DoublyLinkedList;

/**
 * Created by Adrian on 26.11.2016.
 */

public class Waypoints extends DoublyLinkedList<NodeItem> {

    private static Gson gson = new Gson();

    public Waypoints() {}

    public void Load(String waypointsJSON) {
        if(!this.isEmpty())
            this.clear();

        Waypoints deserializedPOIs = gson.fromJson(waypointsJSON, Waypoints.class);
        this.addAll(deserializedPOIs);
    }

    public boolean Save() {
        // TODO save offline into file
        return false;
    }
}
