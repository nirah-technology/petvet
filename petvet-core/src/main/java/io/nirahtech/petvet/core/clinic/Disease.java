package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Disease implements Serializable {
    private static final Map<String, Disease> INSTANCES = new HashMap<>();

    private final String name;
    private final Set<String> symptoms;

    private Disease(final String name) {
        this.name = Objects.requireNonNull(name, "Name for disease is required.");
        this.symptoms = new HashSet<>();
    }

    public final String getName() {
        return this.name;
    }

    public final Set<String> getSymptoms() {
        return this.symptoms;
    }

    public void addSymptoms(String... symptoms) {
        if (Objects.nonNull(symptoms)) {
            this.symptoms.addAll(Set.of(symptoms));
        }
    }

    private static final String sanitize(final String name) {
        return name.toLowerCase();
    }

    public static final Disease of(final String name) {
        final String key = sanitize(name);
        return INSTANCES.computeIfAbsent(key, Disease::new);
    }
}
