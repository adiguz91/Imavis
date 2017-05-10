package com.drone.imavis.mvp.util.constants.classes;

import android.os.Environment;

import com.drone.imavis.mvp.util.FileUtil;

/**
 * Created by adigu on 23.02.2017.
 */

public class CFileDirectories {
    public static final String FLYPLAN_RELATIVE = "/imavis/flyplans/";
    public static final String FLYPLAN_ABSOLUTE = FileUtil.FilePathCombine(
                        Environment.getDataDirectory().getAbsolutePath(), CFileDirectories.FLYPLAN_RELATIVE);
}
