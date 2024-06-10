package io.nirahtech.petvet.features.community;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface DisableDatabaseSharingModeFeature {
    void disableDatabaseSharingMode() throws FeatureExecutionException;
}
