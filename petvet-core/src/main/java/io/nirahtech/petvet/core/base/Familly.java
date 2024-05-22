package io.nirahtech.petvet.core.base;

import java.time.LocalDate;

import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.clinic.HealthBook;

/**
 * Familly
 */
interface Familly {
    HealthBook adopt(final Animal animal, final String name, final LocalDate adoptionDate);
}