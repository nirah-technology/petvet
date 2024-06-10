package io.nirahtech.petvet.features.community;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface SendDatabaseSharedResponseFeature {
    void sendDatabaseSharedResponse(final String response) throws FeatureExecutionException;
}
