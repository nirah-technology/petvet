package io.nirahtech.petvet.features.boot.implementations;

import java.io.File;

import io.nirahtech.petvet.features.boot.DetectFirstBootFeature;

public class DefaultDetectFirstBootFeatureImpl implements DetectFirstBootFeature {

    private final File workingDirectory;

    public DefaultDetectFirstBootFeatureImpl(final File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    @Override
    public Boolean get() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }
    
}
