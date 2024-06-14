package io.nirahtech.petvet.features.planner;

import java.util.Optional;

import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface RetrieveEventDetailFeature {
    Optional<Event> retrieveEventDetail(final Identifier identifierOfTheEventToRetrieve) throws FeatureExecutionException;
}
