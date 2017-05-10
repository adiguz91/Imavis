package com.drone.imavis.mvp.util.constants.classes;

/**
 * Created by Adrian on 26.11.2016.
 */

public class CFlyPlan {

    public static final UnitOfLength UNIT_OF_LENGTH = UnitOfLength.Meter; // default
    public static final int MIN_FLY_HEIGHT = 20; // meter
    public static final int MIN_SPEED = 5; // meter/second
    public static final int MAX_WAYPOINTS_SIZE = 16;
    public static final int MAX_POI_SIZE = 4;

    public enum UnitOfLength {
        Meter, // Europe
        Yard // USA
    }
}

