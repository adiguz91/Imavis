package com.drone.imavis.mvp.util;

/**
 * Created by Adrian on 27.11.2016.
 */

public class StringUtil {
    public static boolean isNullOrEmpty(String source) {
        return !(source != null && !source.isEmpty());
    }
}
