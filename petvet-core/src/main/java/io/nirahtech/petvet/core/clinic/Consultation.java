package io.nirahtech.petvet.core.clinic;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import io.nirahtech.petvet.core.util.Weight;

public final class Consultation {
    private final LocalDateTime dateTime;
    private final Weight weight;
    private final Set<String> observations;
    private final Treatment treatment;

    public Consultation(
        final LocalDateTime dateTime,
        final Weight weight,
        final Set<String> observations,
        final Treatment treatment
    ) {
        this.dateTime = Objects.requireNonNull(dateTime, "Date for consultation is required.");
        this.weight = Objects.requireNonNull(weight, "Weight for consultation is required.");
        this.observations = Objects.requireNonNullElse(observations, new HashSet<>());
        this.treatment = treatment;
    }

    /**
     * @return the dateTime
     */
    public final LocalDateTime getDateTime() {
        return this.dateTime;
    }
    /**
     * @return the observations
     */
    public final Set<String> getObservations() {
        return this.observations;
    }
    /**
     * @return the treatment
     */
    public final Optional<Treatment> getTreatment() {
        return Optional.ofNullable(this.treatment);
    }
    /**
     * @return the weight
     */
    public final Weight getWeight() {
        return this.weight;
    }
}
