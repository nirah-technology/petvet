package io.nirahtech.petvet.core.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.clinic.ConventionalMedicine;
import io.nirahtech.petvet.core.clinic.Medication;
import io.nirahtech.petvet.core.clinic.NaturalMedicine;
import io.nirahtech.petvet.core.pharmacy.Elixir;

public final class Pharmacy implements Serializable {
    private final Set<Medication> medications;
    private final Set<Elixir> elixirs;

    public Pharmacy() {
        this.medications = new HashSet<>();
        this.elixirs = new HashSet<>();
    }

    public void supply(final Medication medication) {
        this.medications.add(medication);
    }

    public void withdraw(final Medication medication) {
        this.medications.remove(medication);
    }

    public Stream<Elixir> getElixirs() {
        return this.elixirs.stream();
    }

    public Stream<Medication> getAllMedications() {
        return this.medications.stream();
    }

    public Stream<NaturalMedicine> getOnlyAllNatualMedecines() {
        return this.getAllMedications()
                .filter(medication -> medication instanceof NaturalMedicine)
                .map(medication -> (NaturalMedicine) medication);
    }

    public Stream<ConventionalMedicine> getOnlyAllConventionalMedicines() {
        return this.getAllMedications()
                .filter(medication -> medication instanceof ConventionalMedicine)
                .map(medication -> (ConventionalMedicine) medication);
    }
}
