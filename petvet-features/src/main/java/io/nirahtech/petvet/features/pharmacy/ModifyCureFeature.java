package io.nirahtech.petvet.features.pharmacy;

import java.util.Map;
import java.util.Set;

import io.nirahtech.petvet.core.base.Ingredient;
import io.nirahtech.petvet.core.pharmacy.Elixir;
import io.nirahtech.petvet.core.pharmacy.ElixirIdentifier;
import io.nirahtech.petvet.core.util.Volume;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;


/**
 * 
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101537
 * @version 1.0
 * 
 * @see java.util.function.Consumer
 * @see io.nirahtech.petvet.core.pharmacy.Elixir
 */
@FunctionalInterface
public interface ModifyCureFeature {
    Elixir modifyCure(
        final ElixirIdentifier identifierOfTheElixirToModify,
        final String name,
        final Set<Ingredient> ingredientsToDelete,
        final Map<Ingredient, Volume> ingredientToAdd
        ) throws FeatureExecutionException;;
}
