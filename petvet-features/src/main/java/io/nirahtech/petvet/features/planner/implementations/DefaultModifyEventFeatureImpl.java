package io.nirahtech.petvet.features.planner.implementations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.core.planning.EventType;
import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.planner.ModifyEventFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultModifyEventFeatureImpl implements ModifyEventFeature {

    private static ModifyEventFeature instance;

    public static final ModifyEventFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Calendar calendar = Calendar.getInstance();
            instance = new DefaultModifyEventFeatureImpl(calendar);
        }
        return instance;
    }

    private final Calendar calendar;

    private DefaultModifyEventFeatureImpl(final Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public Event modifyEvent(
            final Identifier identifierOfTheEventToModify,
            final String name,
            final String description,
            final LocalDateTime dateTime,
            final Duration duration,
            final Boolean isRepeating,
            final Long repeatInterval,
            final ChronoUnit repeatUnit,
            final Integer totalRepeatCycles,
            final EventType eventType) throws FeatureExecutionException {
        Objects.requireNonNull(identifierOfTheEventToModify, "Identifier is required for ModifyEventFeature");
        final Optional<Event> eventToModify = this.calendar.getEvents()
                .filter(event -> event.getIdentifier().equals(identifierOfTheEventToModify)).findFirst();
        if (!eventToModify.isPresent()) {
            throw new FeatureExecutionException(String.format("There is no event with id: %L", identifierOfTheEventToModify.getId()));
        }
        final Event event = eventToModify.get();
        if (Objects.nonNull(name)) {
            event.setName(name);
        }
        if (Objects.nonNull(description)) {
            event.setDescription(description);
        }
        if (Objects.nonNull(dateTime)) {
            event.setDateTime(dateTime);
        }
        if (Objects.nonNull(duration)) {
            event.setDuration(duration);
        }
        if (Objects.nonNull(isRepeating)) {
            event.setRepeating(isRepeating);
        }
        if (Objects.nonNull(repeatInterval)) {
            event.setRepeatInterval(repeatInterval);
        }
        if (Objects.nonNull(repeatUnit)) {
            event.setRepeatUnit(repeatUnit);
        }
        if (Objects.nonNull(totalRepeatCycles)) {
            event.setTotalRepeatCycles(totalRepeatCycles);
        }
        if (Objects.nonNull(eventType)) {
            event.setEventType(eventType);
        }
        return event;
    }

}
