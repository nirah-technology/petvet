package io.nirahtech.petvet.core.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ingredient {
    private static final Map<String, Ingredient> INSTANCES = new HashMap<>();

    private final String name;

    public Ingredient(final String name) {
        this.name = Objects.requireNonNull(name, "Name for ingredient is required.");
    }

    public final String getName() {
        return this.name;
    }

    private static final String sanitize(final String name) {
        return name.toLowerCase();
    }

    public static final Ingredient of(final String name) {
        final String key = sanitize(name);
        return INSTANCES.computeIfAbsent(key, Ingredient::new);
    }
}
