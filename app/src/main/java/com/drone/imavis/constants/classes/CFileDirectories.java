package com.drone.imavis.constants.classes;

import android.os.Environment;

import extensions.file.FileExtension;

/**
 * Created by adigu on 23.02.2017.
 */

public class CFileDirectories {
    public static final String FLYPLAN_RELATIVE = "/imavis/flyplans/";
    public static final String FLYPLAN_ABSOLUTE = FileExtension.FilePathCombine(
                        Environment.getDataDirectory().getAbsolutePath(), CFileDirectories.FLYPLAN_RELATIVE);
}
