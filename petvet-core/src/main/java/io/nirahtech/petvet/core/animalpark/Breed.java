package io.nirahtech.petvet.core.animalpark;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Breed {
    private static final Map<String, Breed> INSTANCES = new HashMap<>();

    private final String name;

    private Breed(final String name) {
        this.name = Objects.requireNonNull(name, "Name for breed is required.");
    }

    public final String getName() {
        return this.name;
    }

    private static final String sanitize(final String name) {
        return name.toLowerCase();
    }

    public static final Breed of(final String name) {
        final String key = sanitize(name);
        return INSTANCES.computeIfAbsent(key, Breed::new);
    }
}
