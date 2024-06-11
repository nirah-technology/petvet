package io.nirahtech.petvet.features.planner.implementations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.core.planning.EventType;
import io.nirahtech.petvet.features.planner.PlannifyEventFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultPlannifyEventFeatureImpl implements PlannifyEventFeature {
    private static PlannifyEventFeature instance;

    public static final PlannifyEventFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Calendar calendar = Calendar.getInstance();
            instance = new DefaultPlannifyEventFeatureImpl(calendar);
        }
        return instance;
    }

    private final Calendar calendar;

    private DefaultPlannifyEventFeatureImpl(final Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public Event plannifyEvent(final String name,
            final String description,
            final LocalDateTime dateTime,
            final Duration duration,
            final Boolean isRepeating,
            final Long repeatInterval,
            final ChronoUnit repeatUnit,
            final Integer totalRepeatCycles,
            final EventType eventType) throws FeatureExecutionException {
        Objects.requireNonNull(name, "Name is required for PlannifyEventFeature");
        Objects.requireNonNull(dateTime, "DateTime is required for PlannifyEventFeature");
        Objects.requireNonNull(isRepeating, "IsRepeating flag is required for PlannifyEventFeature");
        Objects.requireNonNull(totalRepeatCycles, "Total repeat cycles is required for PlannifyEventFeature");
        Objects.requireNonNull(eventType, "Event type is required for PlannifyEventFeature");
        final Event event = new Event(dateTime, duration);
        event.setName(name);
        event.setDescription(description);
        event.setRepeating(isRepeating);
        event.setRepeatInterval(repeatInterval);
        event.setRepeatUnit(repeatUnit);
        event.setTotalRepeatCycles(totalRepeatCycles);
        event.setEventType(eventType);
        this.calendar.add(event);
        return event;
    }

}
