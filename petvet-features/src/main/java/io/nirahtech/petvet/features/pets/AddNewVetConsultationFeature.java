package io.nirahtech.petvet.features.pets;

import java.util.function.BiConsumer;

import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.clinic.Consultation;

/**
 * <p>
 * Interface that represents the feature (also called use case) of adding a new
 * vet consultation.
 * </p>
 * <p>
 * It's a consumer that takes a Pet object and a Consultation object as input.
 * </p>
 * <p>
 * The Pet object is the pet to which the consultation is to be added.
 * </p>
 * <p>
 * The Consultation object is the consultation to be added to the pet.
 * </p>
 * <p>
 * The consultation added to the pet is the consultation with the given
 * consultation object.
 * </p>
 * <p>
 * The pet to which the consultation is added is the pet with the given pet
 * object.
 * </p>
 * <p>
 * The consultation added to the pet is the consultation to be done by a vet to
 * check the health of the pet and to give the pet the necessary treatment to
 * keep the pet healthy and happy and to prevent the pet from getting sick and
 * to treat the pet if the pet gets sick.
 * </p>
 * 
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101357
 * @version 1.0
 * 
 * @see java.util.function.BiConsumer
 * @see io.nirahtech.petvet.core.base.Pet
 * @see io.nirahtech.petvet.core.clinic.Consultation
 */
@FunctionalInterface
public interface AddNewVetConsultationFeature extends BiConsumer<Pet, Consultation> {

}
