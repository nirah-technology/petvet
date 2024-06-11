package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.time.LocalDate;

public final class Vaccination implements Serializable {
    private VaccinationIdentifier identifier;
    private final LocalDate date;
    private final Vaccine vaccine;

    public Vaccination(final LocalDate date, final Vaccine vaccine) {
        this.date = date;
        this.vaccine = vaccine;
    }

    public VaccinationIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(VaccinationIdentifier identifier) {
        this.identifier = identifier;
    }
    public final LocalDate getDate() {
        return this.date;
    }
    public final Vaccine getVaccine() {
        return this.vaccine;
    }
}
