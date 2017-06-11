package com.drone.flyplanner.util.constants.languages;

/**
 * Created by Adrian on 27.11.2016.
 */

public enum Language {
    English ("en"),
    German ("de");

    private final String name;

    private Language(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    /* (non-Javadoc)
    * @see java.lang.Enum#toString()
    */
    @Override
    public String toString() {
        return this.name;
    }
}