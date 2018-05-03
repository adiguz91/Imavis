package com.drone.imavis.mvp.util.constants.languages;

/**
 * Created by Adrian on 27.11.2016.
 */

public enum Language {
    English("en"),
    German("de");

    private final String name;

    Language(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName != null) && name.equals(otherName);
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.name;
    }
}