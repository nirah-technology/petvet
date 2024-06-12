package io.nirahtech.petvet.core.pharmacy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.nirahtech.petvet.core.base.Ingredient;
import io.nirahtech.petvet.core.util.Volume;
import io.nirahtech.petvet.core.util.identifier.Identifier;

/**
 * Elixir
 */
public final class Elixir implements Alchimy {
    private Identifier identifier;
    private final String name;
    private String description;
    private final Map<Ingredient, Volume> composition = new HashMap<>();
    
    public Elixir(final String name) {
        this(name, name, new HashMap<>());
    }

    public Elixir(final String name, final String description) {
        this(name, description, new HashMap<>());
    }

    public Elixir(final String name, final Map<Ingredient, Volume> composition) {
        this(name, null, composition);
    }

    public Elixir(final String name, final String description, final Map<Ingredient, Volume> composition) {
        this.name = name;
        this.description = description;
        this.composition.putAll(composition);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public final String getName() {
        return this.name;
    }

    public final Optional<String> getDescription() {
        return Optional.ofNullable(this.description);
    }

    public final Map<Ingredient, Volume> getComposition() {
        return Collections.unmodifiableMap(this.composition);
    }

    @Override
    public final void addIngredient(final Ingredient ingredient, final Volume volume) {
        this.composition.put(ingredient, volume);
    }

    @Override
    public final void removeIngredient(final Ingredient ingredient) {
        this.composition.remove(ingredient);
    }

    @Override
    public final void changeVolume(final Ingredient ingredient, final Volume volume) {
        if (this.composition.containsKey(ingredient)) {
            this.removeIngredient(ingredient);
        }
        this.addIngredient(ingredient, volume);
    }

}