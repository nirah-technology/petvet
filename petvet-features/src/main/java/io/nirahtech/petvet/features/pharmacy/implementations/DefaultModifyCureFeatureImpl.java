package io.nirahtech.petvet.features.pharmacy.implementations;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import io.nirahtech.petvet.core.base.Ingredient;
import io.nirahtech.petvet.core.base.Pharmacy;
import io.nirahtech.petvet.core.pharmacy.Elixir;
import io.nirahtech.petvet.core.pharmacy.ElixirIdentifier;
import io.nirahtech.petvet.core.util.Volume;
import io.nirahtech.petvet.features.pharmacy.ModifyCureFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultModifyCureFeatureImpl implements ModifyCureFeature {
    private static ModifyCureFeature instance;

    public static final ModifyCureFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Pharmacy pharmacy = Pharmacy.getInstance();
            instance = new DefaultModifyCureFeatureImpl(pharmacy);
        }
        return instance;
    }

    private final Pharmacy pharmacy;

    private DefaultModifyCureFeatureImpl(final Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    @Override
    public Elixir modifyCure(ElixirIdentifier identifierOfTheElixirToModify, String name,
            Set<Ingredient> ingredientsToDelete, Map<Ingredient, Volume> ingredientToAdd)
            throws FeatureExecutionException {
        Objects.requireNonNull(identifierOfTheElixirToModify, "Elixir identifier is required for ModifyCureFeature.");
        Optional<Elixir> elixirFound = this.pharmacy.getElixirs()
                .filter(elixir -> elixir.getIdentifier().equals(identifierOfTheElixirToModify))
                .findFirst();
        if (!elixirFound.isPresent()) {
            throw new FeatureExecutionException(String.format("Elixir not found: %s", identifierOfTheElixirToModify.getId()));
        }
        final Elixir elixir = elixirFound.get();
        if (Objects.nonNull(name)) {
            
        }
        if (Objects.nonNull(ingredientsToDelete)) {
            ingredientsToDelete.forEach(ingredientToDelete -> {
                elixir.removeIngredient(ingredientToDelete);
            });
        }
        if (Objects.nonNull(ingredientToAdd)) {
            ingredientToAdd.entrySet().forEach(ingredientComposition -> {
                elixir.addIngredient(ingredientComposition.getKey(), ingredientComposition.getValue());
            });
        }
        return elixir;
    }

}
