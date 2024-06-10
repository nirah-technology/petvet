package io.nirahtech.petvet.features.planner.implementations;

import java.util.Objects;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.features.planner.RetrieveAllEventsFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRetrieveAllEventsFeatureImpl implements RetrieveAllEventsFeature {
    
    private static RetrieveAllEventsFeature instance;

    public static final RetrieveAllEventsFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Calendar calendar = Calendar.getInstance();
            instance = new DefaultRetrieveAllEventsFeatureImpl(calendar);
        }
        return instance;
    }

    private final Calendar calendar;

    private DefaultRetrieveAllEventsFeatureImpl(final Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public Stream<Event> retrieveAllEvents() throws FeatureExecutionException {
        return this.calendar.getEvents();
    }

}
