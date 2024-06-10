package io.nirahtech.petvet.features.community;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface SendDatabaseSharingAuthRequestFeature {
    void sendDatabaseSharingAuthRequest(final String authRequest) throws FeatureExecutionException;
}
