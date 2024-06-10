package io.nirahtech.petvet.features.emergency;

import java.util.function.Consumer;

import io.nirahtech.petvet.core.clinic.Company;

/**
 * <p>
 * Interface that represents the feature (also called use case) of modifying an
 * emergency contact.
 * </p>
 * <p>
 * It's a consumer that takes a Company object as input.
 * </p>
 * <p>
 * The Company object is the company whose emergency contact is to be modified.
 * </p>
 * <p>
 * The emergency contact of a company is the contact to be used in case of an
 * emergency.
 * </p>
 * <p>
 * The emergency contact of a company is the contact to be used to contact the
 * company in case of an emergency.
 * </p>
 * <p>
 * The company whose emergency contact is modified is the company with the given
 * company object.
 * </p>
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101348
 * @version 1.0
 * 
 * @see java.util.function.Consumer
 * @see io.nirahtech.petvet.core.clinic.Company
 */
@FunctionalInterface
public interface ModifyEmergencyContactFeature extends Consumer<Company> {

}
