package io.nirahtech.petvet.features.planner;

import io.nirahtech.petvet.core.planning.EventIdentifier;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface CancelEventFeature {
    void cancelEvent(final EventIdentifier identifierOfTheEventToCancel) throws FeatureExecutionException;
}
