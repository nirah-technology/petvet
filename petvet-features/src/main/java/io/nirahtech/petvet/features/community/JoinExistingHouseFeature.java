package io.nirahtech.petvet.features.community;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * <p>
 * Interface that represents the feature (also called use case) of joining an
 * existing house.
 * </p>
 * <p>
 * It's a function that takes a string as input and returns an optional House
 * object.
 * </p>
 * <p>
 * The input string is the name of the house to join.
 * </p>
 * <p>
 * The House object returned is the house joined.
 * </p>
 * <p>
 * If the house with the given name does not exist, the optional returned is
 * empty.
 * </p>
 * <p>
 * If the house with the given name exists, the optional returned is the house
 * joined.
 * </p>
 * <p>
 * The house joined is the house with the given name.
 * </p>
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101321
 * @version 1.0
 * 
 * @see java.util.function.Function
 * @see java.util.Optional
 * @see io.nirahtech.petvet.core.base.House
 */
@FunctionalInterface
public interface JoinExistingHouseFeature {
    House joinExistingHouse() throws FeatureExecutionException;
}
