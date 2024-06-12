package io.nirahtech.petvet.features.persistence;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public interface SaveHouseFeature extends PersistentFileManager {
    void saveHouse(final House house) throws FeatureExecutionException;
}
