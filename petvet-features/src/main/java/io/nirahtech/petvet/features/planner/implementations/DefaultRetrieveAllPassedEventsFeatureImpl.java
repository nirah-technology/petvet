package io.nirahtech.petvet.features.planner.implementations;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.features.planner.RetrieveAllPassedEventsFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRetrieveAllPassedEventsFeatureImpl implements RetrieveAllPassedEventsFeature {

    private static RetrieveAllPassedEventsFeature instance;

    public static final RetrieveAllPassedEventsFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Calendar calendar = Calendar.getInstance();
            instance = new DefaultRetrieveAllPassedEventsFeatureImpl(calendar);
        }
        return instance;
    }

    private final Calendar calendar;

    private DefaultRetrieveAllPassedEventsFeatureImpl(final Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public Stream<Event> retrieveAllPassedEvents() throws FeatureExecutionException {
        return this.calendar.getEvents()
                .filter(event -> event.getDateTime()
                        .plusSeconds(event.getDuration().toSeconds())
                        .isBefore(LocalDateTime.now()));
    }

}
