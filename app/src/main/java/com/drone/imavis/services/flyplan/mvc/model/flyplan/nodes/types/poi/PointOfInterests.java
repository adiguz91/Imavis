package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi;

import java.util.ArrayList;

import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.google.gson.Gson;

/**
 * Created by Adrian on 26.11.2016.
 */

public class PointOfInterests extends ArrayList<PointOfInterest> {

    private static Gson gson = new Gson();
    private PointOfInterest selectedPOI;

    public PointOfInterests() {}

    public void Load(String pointOfInterestsJSON) {
        if(!this.isEmpty())
            this.clear();

        PointOfInterests deserializedPOIs = gson.fromJson(pointOfInterestsJSON, PointOfInterests.class);
        this.addAll(deserializedPOIs);
    }

    public PointOfInterest getSelectedPOI() {
        return selectedPOI;
    }

    public void setSelectedPOI(PointOfInterest selectedPOI) {
        this.selectedPOI = selectedPOI;
    }

    public boolean Store() {
        return false;
    }
}
