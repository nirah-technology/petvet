package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.util.Objects;

public abstract class Medication implements Serializable {
    private MedicationIdentifier identifier;
    private final String name;
    private final Prescription prescription;

    protected Medication(
        final String name,
        final Prescription prescription
    ) {
        this.name = Objects.requireNonNull(name, "Name fo medication is required.");
        this.prescription = Objects.requireNonNull(prescription, "Prescription fot medication is required.");
    }

    public MedicationIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(MedicationIdentifier identifier) {
        this.identifier = identifier;
    }
    
    public final String getName() {
        return this.name;
    }
    public final Prescription getPrescription() {
        return this.prescription;
    }

}
