package io.nirahtech.petvet.features.boot.implementations;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.boot.DetectFirstBootFeature;
import io.nirahtech.petvet.features.persistence.implementations.DefaultLoadHouseFeatureImpl;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultDetectFirstBootFeatureImpl implements DetectFirstBootFeature {

    private static final Map<String, DetectFirstBootFeature> INSTANCES = new HashMap<>();
    
    public static final DetectFirstBootFeature getInstance(final File persistenceFile) {
        Objects.requireNonNull(persistenceFile, "Working Directory folder is required for DetectFirstBootFeature.");
        if (!INSTANCES.containsKey(persistenceFile.getAbsolutePath())) {
            INSTANCES.put(persistenceFile.getAbsolutePath(), new DefaultDetectFirstBootFeatureImpl(persistenceFile));
        }
        return INSTANCES.get(persistenceFile.getAbsolutePath());
    }

    private final File persistenceFile;

    private DefaultDetectFirstBootFeatureImpl(final File persistenceFile) {
        this.persistenceFile = persistenceFile;
    }

    public File getWorkingDirectory() {
        return persistenceFile;
    }

    @Override
    public Optional<House> detectFirstBootTryingToRetrieveHouse() throws FeatureExecutionException {
        Optional<House> loadedHouse = Optional.empty();
        if (this.persistenceFile.exists()) {
            if (!this.persistenceFile.isFile()) {
                throw new FeatureExecutionException("");
            } else {
                loadedHouse = DefaultLoadHouseFeatureImpl.getInstance(this.persistenceFile).loadHouse();  
            }
        }
        
        return loadedHouse;
    }
    
}
