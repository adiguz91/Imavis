package com.drone.imavis.util.constants;

import com.drone.imavis.util.constants.classes.CDate;
import com.drone.imavis.util.constants.classes.CFlyPlan;
import com.drone.imavis.util.constants.classes.CMap;
import com.drone.imavis.util.constants.languages.LanguageController;

/**
 * Created by Adrian on 27.11.2016.
 */

public class Constants {

    private LanguageController language;
    private CFlyPlan flyPlan;
    private CMap map;
    private CDate date;

    public Constants() {}

    public LanguageController getLanguage() {
        return language;
    }

    public void setLanguage(LanguageController language) {
        this.language = language;
    }

    public CFlyPlan getFlyPlan() {
        return flyPlan;
    }

    public CMap getMap() {
        return map;
    }

    public CDate getDate() {return date; }
}
