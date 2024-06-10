package io.nirahtech.petvet.features.pharmacy;

import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.clinic.Dosage;
import io.nirahtech.petvet.core.pharmacy.Elixir;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * 
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101537
 * @version 1.0
 * 
 * @see java.util.function.BiFunction
 * @see io.nirahtech.petvet.core.base.Pet
 * @see io.nirahtech.petvet.core.clinic.Dosage
 * @see io.nirahtech.petvet.core.pharmacy.Elixir
 */
@FunctionalInterface
public interface ComputeCureDosageForPetFeature {
    Dosage compiteCureDosageForPet(final Elixir elixir, final Pet pet) throws FeatureExecutionException;
}
