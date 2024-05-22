package io.nirahtech.petvet.core.clinic;

import io.nirahtech.petvet.core.base.Pet;

public interface Healer {
    HealthBook heal(final Pet pet, final Consultation consultation);
    HealthBook heal(final Pet pet, final Consultation consultation, final HealthBook healthBook);
}
