package io.nirahtech.petvet.features.community;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface RetrieveDatabaseSharingAuthRequestFeature {
    String retrieveDatabaseSharingAuthRequest() throws FeatureExecutionException;
}
