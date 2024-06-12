package io.nirahtech.petvet.features.persistence;

import java.util.Optional;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface LoadHouseFeature extends PersistentFileManager {
    Optional<House> loadHouse() throws FeatureExecutionException;
}
