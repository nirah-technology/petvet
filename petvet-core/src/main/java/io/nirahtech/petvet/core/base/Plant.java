package io.nirahtech.petvet.core.base;

import java.util.HashMap;
import java.util.Map;

public final class Plant extends Ingredient {
    private static final Map<String, Plant> INSTANCES = new HashMap<>();

    private Plant(final String name) {
        super(name);
    }

    public static final Plant of(final String name) {
        final String key = sanitize(name);
        return INSTANCES.computeIfAbsent(key, Plant::new);
    }
}
