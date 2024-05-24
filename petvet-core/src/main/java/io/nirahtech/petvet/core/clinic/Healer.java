package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;

import io.nirahtech.petvet.core.base.Pet;

public interface Healer extends Serializable {
    HealthBook heal(final Pet pet, final Consultation consultation);
    HealthBook heal(final Pet pet, final Consultation consultation, final HealthBook healthBook);
}
