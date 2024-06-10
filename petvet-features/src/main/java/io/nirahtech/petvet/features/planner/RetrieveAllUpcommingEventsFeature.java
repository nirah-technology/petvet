package io.nirahtech.petvet.features.planner;

import java.util.function.Supplier;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.planning.Event;

public interface RetrieveAllUpcommingEventsFeature extends Supplier<Stream<Event>> {
    
}
