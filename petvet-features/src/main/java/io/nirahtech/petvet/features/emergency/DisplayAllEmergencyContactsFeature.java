package io.nirahtech.petvet.features.emergency;

import java.util.function.Supplier;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.clinic.Company;

/**
 * <p>
 * Interface that represents the feature (also called use case) of displaying
 * all
 * emergency contacts.
 * </p>
 * <p>
 * It's a supplier that returns a stream of Company objects.
 * </p>
 * <p>
 * The stream of Company objects returned is the stream of all emergency
 * contacts.
 * </p>
 * <p>
 * The emergency contacts are the contacts to be used in case of an emergency.
 * </p>
 * <p>
 * The emergency contacts are the contacts to be used to contact the companies
 * in case of an emergency.
 * </p>
 * <p>
 * The companies whose emergency contacts are displayed are the companies with
 * emergency contacts.
 * </p>
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101328
 * @version 1.0
 * 
 * @see java.util.function.Supplier
 * @see java.util.stream.Stream
 * @see io.nirahtech.petvet.core.clinic.Company
 */
@FunctionalInterface
public interface DisplayAllEmergencyContactsFeature extends Supplier<Stream<Company>> {

}
