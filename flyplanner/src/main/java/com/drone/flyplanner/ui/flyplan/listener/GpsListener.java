package com.drone.flyplanner.ui.flyplan.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by adigu on 10.03.2017.
 */

/*---------- Listener class to get coordinates ------------- */
public class GpsListener implements LocationListener {

    private static Location gpsLocation;
    public static Location getGPSLocation() { return gpsLocation; }

    @Override
    public void onLocationChanged(Location gpsLocationCurrent) {
        Log.w("GpsListener", "gpsLocationCurrent: " + gpsLocationCurrent);
        gpsLocation = gpsLocationCurrent;
        /*
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}
}