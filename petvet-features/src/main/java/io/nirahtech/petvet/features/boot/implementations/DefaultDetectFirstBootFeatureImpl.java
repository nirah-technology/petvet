package io.nirahtech.petvet.features.boot.implementations;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.boot.DetectFirstBootFeature;
import io.nirahtech.petvet.features.util.FeaturesConfiguration;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultDetectFirstBootFeatureImpl implements DetectFirstBootFeature {

    private static final Map<String, DetectFirstBootFeature> INSTANCES = new HashMap<>();
    private static final String DATABASE_FILE_NAME = FeaturesConfiguration.DATABASE_FILE_NAME;
    
    public static final DetectFirstBootFeature getInstance(final File workingDirectory) {
        Objects.requireNonNull(workingDirectory, "Working Directory folder is required for DetectFirstBootFeature.");
        if (!INSTANCES.containsKey(workingDirectory.getAbsolutePath())) {
            INSTANCES.put(workingDirectory.getAbsolutePath(), new DefaultDetectFirstBootFeatureImpl(workingDirectory));
        }
        return INSTANCES.get(workingDirectory.getAbsolutePath());
    }

    private final File workingDirectory;

    private DefaultDetectFirstBootFeatureImpl(final File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    @Override
    public Optional<House> detectFirstBootTryingToRetrieveHouse() throws FeatureExecutionException {
        if (this.workingDirectory.exists()) {
            if (this.workingDirectory.isFile()) {
                throw new FeatureExecutionException("");
            } else {
                final File file = new File(this.workingDirectory, DATABASE_FILE_NAME);
                if (file.exists()) {
                    if (file.isDirectory()) {
                        throw new FeatureExecutionException("");
                    } else {
                        // Load serialize House from file
                    }
                }  
            }
        }
        
        return Optional.empty();
    }
    
}
