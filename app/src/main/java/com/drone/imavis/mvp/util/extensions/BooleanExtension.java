package com.drone.imavis.mvp.util.extensions;

/**
 * Created by adigu on 26.12.2017.
 */

public class BooleanExtension {

    public static boolean parse(byte value) {
        return value == 1 ? true:false;
    }
}
