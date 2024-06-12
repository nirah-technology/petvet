package io.nirahtech.petvet.features.emergency;

import java.time.LocalDate;

import io.nirahtech.petvet.core.clinic.Vet;
import io.nirahtech.petvet.core.contact.Contact;
import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

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
public interface ModifyVetFeature {
    Vet modifyVetContact(
        final Identifier vetIdenfitifer,
        final String firstName,
        final String lastName,
        final LocalDate birthDate,
        final Contact humanContact,
        final String companyName,
        final Contact companyContact,
        final Contact vetContact
    ) throws FeatureExecutionException;
}
