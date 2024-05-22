package io.nirahtech.petvet.core.clinic;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import io.nirahtech.petvet.core.base.Pet;

public final class HealthBook {
    private final Pet pet;
    private final Set<Consultation> consultations;

    public HealthBook(final Pet pet) {
        this.pet = Objects.requireNonNull(pet, "Pet for healthbook is required.");
        this.consultations = new LinkedHashSet<>();
    }

    /**
     * @return the pet
     */
    public final Pet getPet() {
        return this.pet;
    }

    /**
     * @return the consultations
     */
    public final List<Consultation> getConsultations() {
        return Collections.unmodifiableList(this.consultations.stream().toList());
    }

    public final void addConsultation(final Consultation consultation) {
        if (Objects.nonNull(consultation)) {
            this.consultations.add(consultation);
        }
    }

}
