package io.nirahtech.petvet.core.base;

import java.io.Serializable;
import java.time.LocalDate;

import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.clinic.HealthBook;

/**
 * Familly
 */
interface AbleToAdopt extends Serializable {
    HealthBook adopt(final Animal animal, final String name, final LocalDate adoptionDate);
}