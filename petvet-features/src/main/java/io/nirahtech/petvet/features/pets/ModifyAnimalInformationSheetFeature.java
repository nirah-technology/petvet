package io.nirahtech.petvet.features.pets;

import java.io.File;
import java.time.LocalDate;

import io.nirahtech.petvet.core.animalpark.Breed;
import io.nirahtech.petvet.core.animalpark.Gender;
import io.nirahtech.petvet.core.animalpark.Species;
import io.nirahtech.petvet.core.base.Microship;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.base.PetIdentifier;
import io.nirahtech.petvet.core.util.Weight;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * 
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101537
 * @version 1.0
 * 
 * @see java.util.function.Consumer
 * @see io.nirahtech.petvet.core.base.Pet
 */
@FunctionalInterface
public interface ModifyAnimalInformationSheetFeature {
    Pet modifyAnimalInformationSheet(
        final PetIdentifier petIdentifier,
        final Species species,
        final Breed breed,
        final Gender gender,
        final LocalDate birthDate,
        final LocalDate deathDate,
        final Weight weight,
        final File picture,
        final String name,
        final LocalDate adoptionDate,
        final Boolean isTatooed,
        final Microship microship
    ) throws FeatureExecutionException;
}
