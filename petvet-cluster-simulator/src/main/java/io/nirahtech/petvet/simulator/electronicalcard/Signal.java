package io.nirahtech.petvet.simulator.electronicalcard;

import java.io.Serializable;

public final class Signal implements Serializable {
    public static final int WIFI_MAX_COVERAGE_IN_CENTIMETERS = 20000 / 100;
    public static final int BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS = 1000 / 100;

    public static final double MINIMUM_SIGNAL_STRENGTH_IN_DBM = -100.0D;
    public static final double MAXIMUM_SIGNAL_STRENGTH_IN_DBM = -30.0D;

    private double strength;

    public Signal() {
        this.strength = MINIMUM_SIGNAL_STRENGTH_IN_DBM;
    }
    public Signal(double strengthInDBm) {
        this.strength = strengthInDBm;
    }

    public double getStrength() {
        return strength;
    }

    public void increaseStrength(double increase) {
        this.setStrength(strength + increase);
    }

    public void decreaseStrength(double decrease) {
        this.setStrength(strength - decrease);
    }

    private void setStrength(double strength) {
        // Signal strength should be between -30 and -100 dBm
        if (strength < MINIMUM_SIGNAL_STRENGTH_IN_DBM || strength > MAXIMUM_SIGNAL_STRENGTH_IN_DBM) {
            throw new IllegalArgumentException("Signal strength should be between -30 and -100 dBm");
        }
        this.strength = strength;
    }

    public static float percentageToDbm(float percentage) {
        return (percentage * 60.0f / 100.0f) - 90.0f;
    }

}
