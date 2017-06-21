package com.drone.flyplanner.data.model.flyplan.nodes.types.poi;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;

import com.drone.flyplanner.DaggerFlyplanner;
import com.drone.flyplanner.util.constants.classes.CColor;
import com.drone.flyplanner.util.flyplan.control.FlyPlanUtil;
import com.drone.flyplanner.util.flyplan.control.IFlyPlanUtil;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by Adrian on 26.11.2016.
 */

public class PointOfInterests extends ArrayList<PointOfInterest> {

    //@Inject
    IFlyPlanUtil flyPlanUtil = FlyPlanUtil.getInstance();

    private static Gson gson = new Gson();
    private PointOfInterest selectedPOI;

    public PointOfInterests() {}

    public int draw(Canvas canvas) {
        int counter = 1;
        int selectedPoiIndex = -1;
        for (PointOfInterest poi : this) {
            poi.setShapePaint();
            if(poi != flyPlanUtil.getSelectedPOI()) {
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
