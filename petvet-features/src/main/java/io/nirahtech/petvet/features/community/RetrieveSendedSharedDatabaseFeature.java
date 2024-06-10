package io.nirahtech.petvet.features.community;

import java.io.File;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface RetrieveSendedSharedDatabaseFeature {
    File retrieveSendedSharedDatabase() throws FeatureExecutionException;
}
