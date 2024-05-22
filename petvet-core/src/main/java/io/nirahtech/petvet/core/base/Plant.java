package io.nirahtech.petvet.core.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Plant {
    private static final Map<String, Plant> INSTANCES = new HashMap<>();

    private final String name;

    private Plant(final String name) {
        this.name = Objects.requireNonNull(name, "Name for plant is required.");
    }

    public final String getName() {
        return this.name;
    }

    private static final String sanitize(final String name) {
        return name.toLowerCase();
    }

    public static final Plant of(final String name) {
        final String key = sanitize(name);
        return INSTANCES.computeIfAbsent(key, Plant::new);
    }
}
