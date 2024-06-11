package io.nirahtech.petvet.features.pets;

import java.util.Optional;

import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.clinic.HealthBook;
import io.nirahtech.petvet.core.clinic.HealthBookIdentifier;
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
 * @see io.nirahtech.petvet.core.clinic.HealthBook
 */
public interface RetrieveHealthBookFeature {
    Optional<HealthBook> retrieveHealthBook(final Animal aniaml) throws FeatureExecutionException;
    Optional<HealthBook> retrieveHealthBook(final Pet pet) throws FeatureExecutionException;
    Optional<HealthBook> retrieveHealthBook(final HealthBookIdentifier healthBookIdentifier) throws FeatureExecutionException;
}
