package io.nirahtech.petvet.features.planner;

import java.util.function.Consumer;

import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface RetrieveEventDetailFeature extends Consumer<Event> {
    Event retrieveEventDetail(final String name) throws FeatureExecutionException;
}
