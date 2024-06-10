package io.nirahtech.petvet.features.pets;

import java.time.LocalDate;

import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.clinic.HealthBook;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * <p>
 * Interface that represents the feature (also called use case) of adopting an
 * animal.
 * </p>
 * <p>
 * It's a functional interface that takes an Animal object, a String object and a
 * LocalDate object as input.
 * </p>
 * <p>
 * The Animal object is the animal to be adopted.
 * </p>
 * <p>
 * The String object is the name of the animal.
 * </p>
 * <p>
 * The LocalDate object is the date of adoption of the animal.
 * </p>
 * <p>
 * The HealthBook object returned is the health book of the adopted animal.
 * </p>
 * <p>
 * The health book of an animal is the book that contains the health information
 * of the animal.
 * </p>
 * <p>
 * The health information of an animal is the information about the health of the
 * animal.
 * </p>
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101328
 * @version 1.0
 * 
 * @see java.util.function.Function
 * @see java.time.LocalDate
 * @see java.util.function.BiFunction
 * @see java.util.stream.Stream
 * @see io.nirahtech.petvet.core.animalpark.Animal
 * @see io.nirahtech.petvet.core.clinic.HealthBook
 
 */
@FunctionalInterface
public interface AdoptAnimalFeature {
    HealthBook adoptAnimal(final Animal animal, final String name, final LocalDate adoptionDate) throws FeatureExecutionException;
   
}
