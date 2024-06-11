package io.nirahtech.petvet.features.planner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.core.planning.EventType;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface PlannifyEventFeature {
    Event plannifyEvent(
        final String name,
        final String description,
        final LocalDateTime dateTime,
        final Duration duration,
        final Boolean isRepeating,
        final Long repeatInterval,
        final ChronoUnit repeatUnit,
        final Integer totalRepeatCycles,
        final EventType eventType
    ) throws FeatureExecutionException;
}
