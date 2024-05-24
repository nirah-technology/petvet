package io.nirahtech.petvet.core.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ingredient implements Serializable {
    private static final Map<String, Ingredient> INSTANCES = new HashMap<>();

    private final String name;

    protected Ingredient(final String name) {
        this.name = Objects.requireNonNull(name, "Name for ingredient is required.");
    }

    public final String getName() {
        return this.name;
    }

    protected static final String sanitize(final String name) {
        return name.toLowerCase();
    }

    public static Ingredient of(final String name) {
        final String key = sanitize(name);
        return INSTANCES.computeIfAbsent(key, Ingredient::new);
    }
}
