package io.nirahtech.petvet.features.planner.implementations;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.features.planner.RetrieveAllUpcommingEventsFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRetrieveAllUpcommingEventsFeatureImpl implements RetrieveAllUpcommingEventsFeature {
    
    private static RetrieveAllUpcommingEventsFeature instance;

    public static final RetrieveAllUpcommingEventsFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Calendar calendar = Calendar.getInstance();
            instance = new DefaultRetrieveAllUpcommingEventsFeatureImpl(calendar);
        }
        return instance;
    }

    private final Calendar calendar;

    private DefaultRetrieveAllUpcommingEventsFeatureImpl(final Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public Stream<Event> retrieveAllUpcommingEvents() throws FeatureExecutionException {
        return this.calendar.getEvents()
                .filter(event -> event.getDateTime().isAfter(LocalDateTime.now()));
    }

}
