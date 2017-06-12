package com.drone.flyplanner.util.constants;

import com.drone.flyplanner.util.constants.classes.CFlyPlan;
import com.drone.flyplanner.util.constants.classes.CMap;
import com.drone.flyplanner.util.constants.languages.LanguageController;

/**
 * Created by Adrian on 27.11.2016.
 */

public class Constants {

    private LanguageController language;
    private CFlyPlan flyPlan;
    private CMap map;

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
}
