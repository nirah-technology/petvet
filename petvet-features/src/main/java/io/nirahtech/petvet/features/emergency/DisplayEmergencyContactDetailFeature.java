package io.nirahtech.petvet.features.emergency;

import java.util.Optional;
import java.util.function.Function;

import io.nirahtech.petvet.core.clinic.Company;

/**
 * <p>
 * Interface that represents the feature (also called use case) of displaying
 * the emergency contact detail of a company.
 * </p>
 * <p>
 * It's a function that takes a string as input and returns an optional Company
 * object.
 * </p>
 * <p>
 * The input string is the name of the company to display the emergency contact
 * detail.
 * </p>
 * <p>
 * The Company object returned is the company whose emergency contact detail is
 * displayed.
 * </p>
 * <p>
 * If the company with the given name does not exist, the optional returned is
 * empty.
 * </p>
 * <p>
 * If the company with the given name exists, the optional returned is the
 * company whose emergency contact detail is displayed.
 * </p>
 * <p>
 * The company whose emergency contact detail is displayed is the company with
 * the given name.
 * </p>
 * <p>
 * The emergency contact detail of a company is the contact detail to be used in
 * case of an emergency.
 * </p>
 * <p>
 * The emergency contact detail of a company is the contact detail to be used to
 * contact the company in case of an emergency.
 * </p>
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101321
 * @version 1.0
 * 
 * @see java.util.function.Function
 * @see java.util.Optional
 * @see io.nirahtech.petvet.core.clinic.Company
 */
@FunctionalInterface
public interface DisplayEmergencyContactDetailFeature extends Function<String, Optional<Company>> {

}
