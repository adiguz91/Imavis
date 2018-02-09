package com.drone.imavis.mvp.ui.searchwlan;

/**
 * Created by adigu on 09.02.2018.
 */

public enum SignalStrength {
    VeryPoor("Very Poor"),
    Poor("Poor"),
    Fair("Fair"),
    Good("Good"),
    Excellent("Excellent");

    private final String text;

    /**
     * @param text
     */
    SignalStrength(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
