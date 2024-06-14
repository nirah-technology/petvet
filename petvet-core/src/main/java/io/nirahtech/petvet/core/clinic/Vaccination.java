package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.time.LocalDate;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class Vaccination implements Serializable {
    private Identifier identifier;
    private final LocalDate date;
    private final Vaccine vaccine;

    public Vaccination(final LocalDate date, final Vaccine vaccine) {
        this.date = date;
        this.vaccine = vaccine;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }
    public final LocalDate getDate() {
        return this.date;
    }
    public final Vaccine getVaccine() {
        return this.vaccine;
    }
}
