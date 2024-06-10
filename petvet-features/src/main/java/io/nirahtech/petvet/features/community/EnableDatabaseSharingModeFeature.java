package io.nirahtech.petvet.features.community;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface EnableDatabaseSharingModeFeature {
    void enableDatabaseSharingMode() throws FeatureExecutionException;
}
