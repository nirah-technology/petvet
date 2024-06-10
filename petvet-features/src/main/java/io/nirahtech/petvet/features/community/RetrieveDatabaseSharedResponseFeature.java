package io.nirahtech.petvet.features.community;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface RetrieveDatabaseSharedResponseFeature {
    byte[] retrieveDatabaseSharedResponse() throws FeatureExecutionException;
}
