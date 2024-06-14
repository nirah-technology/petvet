package io.nirahtech.petvet.features.pets;

import java.util.Optional;

import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * <p>
 * Interface that represents the feature (also called use case) of displaying
 * the animal information sheet of a pet.
 * </p>
 * <p>
 * It's a function that takes a string as input and returns an optional Pet
 * object.
 * </p>
 * <p>
 * The input string is the name of the pet to display the animal information
 * sheet.
 * </p>
 * <p>
 * The Pet object returned is the pet whose animal information sheet is
 * displayed.
 * </p>
 * <p>
 * If the pet with the given name does not exist, the optional returned is
 * empty.
 * </p>
 * <p>
 * If the pet with the given name exists, the optional returned is the pet whose
 * animal information sheet is displayed.
 * </p>
 * <p>
 * The pet whose animal information sheet is displayed is the pet with the given
 * name.
 * </p>
 * <p>
 * The animal information sheet of a pet is the sheet that contains the
 * information about the pet.
 * </p>
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101424
 * @version 1.0
 * 
 * @see java.util.function.Function
 * @see java.util.Optional
 * @see io.nirahtech.petvet.core.base.Pet
 
 */
@FunctionalInterface
public interface RetrieveAnimalInformationSheetFeature {
    Optional<Pet> retrieveAnimalInformationSheet(final Identifier petIdentifier) throws FeatureExecutionException;
}
