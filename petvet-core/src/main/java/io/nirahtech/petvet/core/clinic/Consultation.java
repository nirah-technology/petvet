package io.nirahtech.petvet.core.clinic;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import io.nirahtech.petvet.core.util.Weight;

public final class Consultation {
    private final LocalDateTime dateTime;
    private final Weight weight;
    private final Set<String> observations;
    private Set<Medication> prescribesMedications;

    public Consultation(
        final LocalDateTime dateTime,
        final Weight weight,
        final Set<String> observations,
        final Set<Medication> prescribesMedications
    ) {
        this.dateTime = Objects.requireNonNull(dateTime, "Date for consultation is required.");
        this.weight = Objects.requireNonNull(weight, "Weight for consultation is required.");
        this.observations = Objects.requireNonNullElse(observations, new HashSet<>());
        this.prescribesMedications = new HashSet<>(Objects.requireNonNullElse(prescribesMedications, new HashSet<>()));
    }

    public final LocalDateTime getDateTime() {
        return this.dateTime;
    }
    public final Set<String> getObservations() {
        return Collections.unmodifiableSet(this.observations);
    }

    public final Set<Medication> getPrescribesMedications() {
        return Collections.unmodifiableSet(this.prescribesMedications);
    }
    public final Weight getWeight() {
        return this.weight;
    }

}
