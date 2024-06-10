package io.nirahtech.petvet.features.community;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface DisableScannerModeToDetectSharedDatabasesFeature {
    void disableScannerModeToDetectSharedDatabases() throws FeatureExecutionException;
}
