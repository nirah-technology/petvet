package io.nirahtech.petvet.features.planner.implementations;

import java.util.Objects;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.EventIdentifier;
import io.nirahtech.petvet.features.planner.CancelEventFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultCancelEventFeatureImpl implements CancelEventFeature {

    private static CancelEventFeature instance;

    public static final CancelEventFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Calendar calendar = Calendar.getInstance();
            instance = new DefaultCancelEventFeatureImpl(calendar);
        }
        return instance;
    }

    private final Calendar calendar;

    private DefaultCancelEventFeatureImpl(final Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public void cancelEvent(final EventIdentifier identifierOfTheEventToCancel) throws FeatureExecutionException {
        Objects.requireNonNull(identifierOfTheEventToCancel, "Event is required for CancelEventFeature");
        this.calendar.getEvents()
                .filter(event -> event.getIdentifier().equals(identifierOfTheEventToCancel))
                .findFirst()
                .ifPresent(eventToCancel -> {
                    this.calendar.delete(eventToCancel);
                });
    }

}
