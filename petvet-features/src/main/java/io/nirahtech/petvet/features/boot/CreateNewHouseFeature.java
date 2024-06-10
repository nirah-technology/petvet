package io.nirahtech.petvet.features.boot;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * <p>
 * Interface that represents the feature (also called use case) of creating a
 * new house.
 * </p>
 * <p>
 * It's a function that takes a string as input and returns a House object.
 * </p>
 * <p>
 * The input string is the name of the house to be created.
 * </p>
 * <p>
 * The House object returned is the newly created house.
 * </p>
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101321
 * @version 1.0
 * 
 * @see io.nirahtech.petvet.core.base.House
 */
@FunctionalInterface
public interface CreateNewHouseFeature {
    House createNewHouse(final String nameOfTheHouse) throws FeatureExecutionException;
}
