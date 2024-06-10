package io.nirahtech.petvet.features.boot;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * <p>
 * Interface that represents the feature (also called use case) of detecting if
 * it's the first boot of the application.
 * </p>
 * <p>
 * It's a supplier that returns a boolean value.
 * </p>
 * <p>
 * The boolean value returned is true if it's the first boot of the application,
 * false otherwise.
 * </p>
 * <p>
 * The first boot of the application is the first time the application is run on
 * a device.
 * </p>
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101321
 * @version 1.0
 * 
 */
@FunctionalInterface
public interface DetectFirstBootFeature {
    boolean isFirstBoot() throws FeatureExecutionException;
}
