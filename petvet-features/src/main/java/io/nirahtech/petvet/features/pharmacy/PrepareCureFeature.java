package io.nirahtech.petvet.features.pharmacy;

import java.util.Map;
import java.util.function.BiFunction;

import io.nirahtech.petvet.core.base.Ingredient;
import io.nirahtech.petvet.core.clinic.Dosage;
import io.nirahtech.petvet.core.pharmacy.Elixir;


/**
 * 
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101537
 * @version 1.0
 * 
 * @see java.util.function.BiFunction
 * @see io.nirahtech.petvet.core.base.Ingredient
 * @see io.nirahtech.petvet.core.clinic.Dosage
 * @see io.nirahtech.petvet.core.pharmacy.Elixir
 */
@FunctionalInterface
public interface PrepareCureFeature extends BiFunction<String, Map<Ingredient, Dosage>, Elixir> {
    
}
