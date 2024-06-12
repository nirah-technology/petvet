package io.nirahtech.petvet.features.planner;

import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface CancelEventFeature {
    void cancelEvent(final Identifier identifierOfTheEventToCancel) throws FeatureExecutionException;
}
