package com.drone.imavis.mvp.data.model;

import android.graphics.Point;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.GPSCoordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.map.Map;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by adigu on 31.12.2017.
 */

public class GoogleMapExtension extends Map<GoogleMap> {

    @Override
    public GPSCoordinate getGpsfromScreen(Coordinate coordinate) {
        Projection projection = getMap().getProjection();
        // TODO toPoint in COORDINATE
        Point screenPoint = new Point((int)coordinate.getX(), (int)coordinate.getY());
        LatLng latLng = projection.fromScreenLocation(screenPoint);
        return new GPSCoordinate(latLng.latitude, latLng.longitude, 0);
    }
}
