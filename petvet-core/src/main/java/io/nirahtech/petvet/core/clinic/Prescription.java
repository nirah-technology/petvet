package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

public class Prescription implements Serializable {
    private PrescriptionIdentifier identifier;
    private final Dosage dosage;
    private final Duration durationInDays;
    private final LocalDate startDate;

    public Prescription(
    final Dosage dosage,
    final Duration durationInDays,
    final LocalDate startDate
    ) {
        this.dosage = dosage;
        this.durationInDays = durationInDays;
        this.startDate = startDate;
    }
    public PrescriptionIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(PrescriptionIdentifier identifier) {
        this.identifier = identifier;
    }

    public final Dosage getDosage() {
        return this.dosage;
    }
    public final Duration getDurationInDays() {
        return this.durationInDays;
    }
    public final LocalDate getStartDate() {
        return this.startDate;
    }
}
