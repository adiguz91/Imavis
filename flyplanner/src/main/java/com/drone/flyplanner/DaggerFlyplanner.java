package com.drone.flyplanner;

import com.drone.flyplanner.di.component.ApplicationComponent;
import com.drone.flyplanner.di.component.DaggerApplicationComponent;
import com.drone.flyplanner.di.module.ApplicationModule;

/**
 * Created by adigu on 13.06.2017.
 */

public class DaggerFlyplanner {
    /*
    private static ApplicationComponent flyplannerComponent;

    public static ApplicationComponent getComponent() {
        if (flyplannerComponent == null) {
            flyplannerComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule())
                    .build();
        }
        return flyplannerComponent;
    }*/
}
