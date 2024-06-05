package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.time.LocalDate;

public final class Vaccination implements Serializable {
    private final LocalDate date;
    private final Vaccine vaccine;

    public Vaccination(final LocalDate date, final Vaccine vaccine) {
        this.date = date;
        this.vaccine = vaccine;
    }
    public final LocalDate getDate() {
        return this.date;
    }
    public final Vaccine getVaccine() {
        return this.vaccine;
    }
}
