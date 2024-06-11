package io.nirahtech.petvet.features.planner;

import io.nirahtech.petvet.core.planning.EventIdentifier;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface ModifyEventFeature {
    void modifyEvent(final EventIdentifier identifierOfTheEventToModify) throws FeatureExecutionException;
}
