package io.nirahtech.petvet.features.community;

import java.util.stream.Stream;

import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface DetectSharedDatabasesOnScannerFeature {
    Stream<String> detectSharedDatabasesOnScanner() throws FeatureExecutionException;;
}
