package com.drone.imavis.util.constants.languages;

/**
 * Created by Adrian on 27.11.2016.
 */

public enum Region {
    USA ("US"),
    German ("DE"),
    Austria ("AT");

    private final String name;

    private Region(String s) {
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
