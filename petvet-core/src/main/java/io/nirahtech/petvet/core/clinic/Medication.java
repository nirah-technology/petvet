package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.util.Objects;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public abstract class Medication implements Serializable {
    private Identifier identifier;
    private final String name;
    private final Prescription prescription;

    protected Medication(
        final String name,
        final Prescription prescription
    ) {
        this.name = Objects.requireNonNull(name, "Name fo medication is required.");
        this.prescription = Objects.requireNonNull(prescription, "Prescription fot medication is required.");
    }

    public Identifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }
    
    public final String getName() {
        return this.name;
    }
    public final Prescription getPrescription() {
        return this.prescription;
    }

}
