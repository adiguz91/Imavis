package com.drone.imavis.mvp.services.dronecontrol;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.drone.imavis.mvp.di.ActivityContext;
import com.drone.imavis.mvp.di.PerActivity;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by adigu on 02.09.2017.
 */

@PerActivity
public class DronePermissionRequestHelper {

    private Context context;

    private String[] permissions;
    private int requestCode;

    @Inject
    public DronePermissionRequestHelper(@ActivityContext Context context) {
        this.context = context;
    }

    public void requestPermission(String[] permissions, int requestCode) {
        if (permissions == null)
            return;

        Set<String> permissionsToRequest = new HashSet<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    Toast.makeText(context, "Please allow permission " + permission, Toast.LENGTH_LONG).show();
                    ((Activity) context).finish();
                    return;
                } else {
                    permissionsToRequest.add(permission);
                }
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions((Activity) context,
                    permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                    requestCode);
        }
    }


}
