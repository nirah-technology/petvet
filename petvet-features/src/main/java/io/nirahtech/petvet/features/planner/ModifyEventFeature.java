package io.nirahtech.petvet.features.planner;

import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface ModifyEventFeature {
    void modifyEvent(final Event eventToModify) throws FeatureExecutionException;
}
