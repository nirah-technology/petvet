package io.nirahtech.petvet.features.planner;

import java.util.function.Supplier;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.planning.Event;

public interface RetrieveAllPassedEventsFeature extends Supplier<Stream<Event>> {
    
}
