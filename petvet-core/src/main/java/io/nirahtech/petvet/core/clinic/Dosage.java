package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;

public final class Dosage implements Serializable {
    private final int timesPerDay;
    private final float quantityEachTime;
    private final String dosage;

    public Dosage(
        final int timesPerDay,
        final float quantityEachTime,
        final String dosage
    ) {
        this.timesPerDay = timesPerDay;
        this.quantityEachTime = quantityEachTime;
        this.dosage = dosage;
    }

    public final String getDosage() {
        return this.dosage;
    }
    public final float getQuantityEachTime() {
        return this.quantityEachTime;
    }
    public final int getTimesPerDay() {
        return this.timesPerDay;
    }
}
