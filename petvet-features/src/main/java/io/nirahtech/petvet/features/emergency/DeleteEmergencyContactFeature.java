package io.nirahtech.petvet.features.emergency;

import io.nirahtech.petvet.core.clinic.Company;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * <p>
 * Interface that represents the feature (also called use case) of deleting an
 * emergency contact.
 * </p>
 * <p>
 * It's a consumer that takes a Company object as input.
 * </p>
 * <p>
 * The Company object is the company whose emergency contact is to be deleted.
 * </p>
 * <p>
 * The emergency contact of a company is the contact to be used in case of an
 * emergency.
 * </p>
 * <p>
 * The emergency contact of a company is the contact to be used to contact the
 * company in case of an emergency.
 * </p>
 *
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101325
 * @version 1.0
 * 
 * @see java.util.function.Consumer
 * @see io.nirahtech.petvet.core.clinic.Company
 * 
 */
@FunctionalInterface
public interface DeleteEmergencyContactFeature {
    void deleteEmergencyContact(final Company emergencyContactToDelete) throws FeatureExecutionException;
}
