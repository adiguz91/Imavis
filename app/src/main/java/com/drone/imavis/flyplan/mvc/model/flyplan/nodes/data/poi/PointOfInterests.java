package com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.poi;

import java.util.ArrayList;
import com.google.gson.Gson;

/**
 * Created by Adrian on 26.11.2016.
 */

public class PointOfInterests extends ArrayList<PointOfInterest> {

    private static Gson gson = new Gson();

    public PointOfInterests() {}

    public void Load(String pointOfInterestsJSON) {
        if(!this.isEmpty())
            this.clear();

        PointOfInterests deserializedPOIs = gson.fromJson(pointOfInterestsJSON, PointOfInterests.class);
        this.addAll(deserializedPOIs);
    }

    public boolean Store() {
        return false;
    }
}
