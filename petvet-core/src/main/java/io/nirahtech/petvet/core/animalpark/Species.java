package io.nirahtech.petvet.core.animalpark;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Species implements Serializable {

    private static final Map<String, Species> INSTANCES = new HashMap<>();

    private final String name;

    private Species(final String name) {
        this.name = Objects.requireNonNull(name, "Name for species is required.");
    }

    public final String getName() {
        return this.name;
    }

    private static final String sanitize(final String name) {
        return name.toLowerCase();
    }

    public static final Species of(final String name) {
        final String key = sanitize(name);
        return INSTANCES.computeIfAbsent(key, Species::new);
    }
}
