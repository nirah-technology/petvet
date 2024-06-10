package io.nirahtech.petvet.features.planner;

import java.util.stream.Stream;

import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface RetrieveAllEventsFeature {
    Stream<Event> retrieveAllEvents() throws FeatureExecutionException;
}
