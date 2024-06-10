package io.nirahtech.petvet.features.planner.implementations;

import java.util.Objects;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.Event;
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
    public void cancelEvent(final Event eventToCancel) throws FeatureExecutionException {
        Objects.requireNonNull(eventToCancel, "Event is required for CancelEventFeature");
        final boolean existsEvent = this.calendar.getEvents().filter(event -> event.equals(eventToCancel)).findFirst().isPresent();
        if (existsEvent) {
            this.calendar.delete(eventToCancel);
        }
    }
    
}
