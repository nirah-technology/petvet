package io.nirahtech.petvet.features.planner.implementations;

import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.planner.RetrieveEventDetailFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRetrieveEventDetailFeatureImpl implements RetrieveEventDetailFeature {
    private static RetrieveEventDetailFeature instance;

    public static final RetrieveEventDetailFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Calendar calendar = Calendar.getInstance();
            instance = new DefaultRetrieveEventDetailFeatureImpl(calendar);
        }
        return instance;
    }

    private final Calendar calendar;

    private DefaultRetrieveEventDetailFeatureImpl(final Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public Optional<Event> retrieveEventDetail(Identifier identifierOfTheEventToRetrieve) throws FeatureExecutionException {
        Objects.requireNonNull(identifierOfTheEventToRetrieve, "Identifier is required for RetrieveEventDetailFeature");
        return this.calendar.getEvents()
                .filter(event -> event.getIdentifier().equals(identifierOfTheEventToRetrieve))
                .findFirst();
    }

}
