package io.nirahtech.petvet.features.community;

import java.io.File;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface SendSharedDatabaseFeature {
    void sendSharedDatabase(final File database) throws FeatureExecutionException;
}
