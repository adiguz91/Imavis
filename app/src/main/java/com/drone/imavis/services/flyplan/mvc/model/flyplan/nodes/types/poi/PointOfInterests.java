package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;

import com.drone.imavis.util.constants.classes.CColor;
import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.google.gson.Gson;

/**
 * Created by Adrian on 26.11.2016.
 */

public class PointOfInterests extends ArrayList<PointOfInterest> {

    private static Gson gson = new Gson();
    private PointOfInterest selectedPOI;

    public PointOfInterests() {}

    public int draw(Canvas canvas) {
        int counter = 1;
        int selectedPoiIndex = -1;
        for (PointOfInterest poi : this) {
            poi.setShapePaint();
            if(poi != FlyPlanController.getSelectedPOI()) {
                poi.getShape().setBackgroundColor(getPoiColorById(counter - 1));
                poi.draw(canvas, String.valueOf(counter));
            } else
                selectedPoiIndex = counter - 1;
            counter++;
        }
        return selectedPoiIndex;
    }

    public int getPoiColorById(int id) {
        return Color.parseColor(CColor.POI_CIRCLES.get(id));
    }

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
