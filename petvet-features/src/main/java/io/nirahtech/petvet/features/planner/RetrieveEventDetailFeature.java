package io.nirahtech.petvet.features.planner;

import java.util.Optional;
import java.util.function.Consumer;

import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.core.planning.EventIdentifier;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface RetrieveEventDetailFeature extends Consumer<Event> {
    Optional<Event> retrieveEventDetail(final EventIdentifier eventIdentifier) throws FeatureExecutionException;
}
