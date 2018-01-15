package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;

/**
 * Created by adigu on 15.01.2018.
 */

public interface OnScreenCoordinateCallback {
    void onScreenCoordinate(Coordinate screenCoordinate);
}
