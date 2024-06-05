package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Vaccine implements Serializable {
    private static final Map<String, Vaccine> INSTANCES = new HashMap<>();

    private final String name;
    private final Duration booster;

    private Vaccine(final String name, final Duration booster) {
        this.name = Objects.requireNonNull(name, "Name for vaccine is required.");
        this.booster = Objects.requireNonNull(booster, "Booster for vaccine is required.");
    }

    public final String getName() {
        return this.name;
    }

    public final Duration getBooster() {
        return this.booster;
    }

    private static final String sanitize(final String name) {
        return name.toLowerCase();
    }

    public static final Vaccine of(final String name, final Duration booster) {
        final String key = sanitize(name);
        if (!INSTANCES.containsKey(key)) {
            final Vaccine vaccine = new Vaccine(name, booster);
            INSTANCES.put(key, vaccine);
        }
        return INSTANCES.get(key);
    }
}
