package com.drone.flyplanner.util.constants.languages;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Adrian on 27.11.2016.
 */

public class LanguageController {

    //private Language language = Language.English;
    //private Region region = Region.USA;
    private String languagePropertieName = "Language";
    private Locale locale = Locale.ENGLISH;
    private ResourceBundle resourceBundle;

    public LanguageController() {}

    private ResourceBundle LoadResourceBundle() {
        return ResourceBundle.getBundle(languagePropertieName, getLocale());
        //Enumeration<String> keys = resourceBundle.getKeys();
        //while (keys.hasMoreElements()) {
            //String key = keys.nextElement();
            //String value = rb.getString(key);
            //System.out.println(key + ": " + value);
        //}
    }

    public ResourceBundle getResourceBundle() {
        if(resourceBundle == null)
            resourceBundle = LoadResourceBundle();
        return resourceBundle;
    }

    private void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public Locale getLocale() {
        //if(locale == null) {
        //    locale = new Locale(getLanguage().toString(), getRegion().toString());}
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        setResourceBundle(LoadResourceBundle());
    }

//    public Region getRegion() {
//        return region;
//    }

//    public void setRegion(Region region) {
//        this.region = region;
//    }

//    public Language getLanguage() {
//        return language;
//    }

//    public void setLanguage(Language language) {
//        this.language = language;
//    }

}


