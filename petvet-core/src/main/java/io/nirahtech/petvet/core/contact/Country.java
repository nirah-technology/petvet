package io.nirahtech.petvet.core.contact;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Country {
    private static final Map<String, Country> INSTANCES = new HashMap<>();

    private final String name;

    private Country(final String name) {
        this.name = Objects.requireNonNull(name, "Country name is required.");
    }

    public final String getName() {
        return this.name;
    }

    private static final String sanitize(final String name) {
        return name.toLowerCase();
    }

    public static final Country of(final String name) {
        final String key = sanitize(name);
        return INSTANCES.computeIfAbsent(key, Country::new);
    }
}
