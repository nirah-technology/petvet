package io.nirahtech.petvet.features.planner.implementations;

import java.util.Objects;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.Event;
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
    public void plannifyEvent(Event eventToPlannify) throws FeatureExecutionException {
        Objects.requireNonNull(eventToPlannify, "Event is required for PlannifyEventFeature");
        this.calendar.add(eventToPlannify);
    }
    
}
