package io.nirahtech.petvet.features;

import java.io.File;

import io.nirahtech.petvet.features.boot.CreateNewHouseFeature;
import io.nirahtech.petvet.features.boot.DetectFirstBootFeature;
import io.nirahtech.petvet.features.boot.implementations.DefaultCreateNewHouseFeatureImpl;
import io.nirahtech.petvet.features.boot.implementations.DefaultDetectFirstBootFeatureImpl;
import io.nirahtech.petvet.features.planner.CancelEventFeature;
import io.nirahtech.petvet.features.planner.PlannifyEventFeature;
import io.nirahtech.petvet.features.planner.implementations.DefaultCancelEventFeatureImpl;
import io.nirahtech.petvet.features.planner.implementations.DefaultPlannifyEventFeatureImpl;

public final class FeaturesRegistry {

    private static FeaturesRegistry instance = new FeaturesRegistry();

    public static FeaturesRegistry getInstance() {
        return instance;
    }

    private FeaturesRegistry() {
        
    }

    public final DetectFirstBootFeature detectFirstBootFeature(final File workingDirectory) {
        return DefaultDetectFirstBootFeatureImpl.getInstance(null);
    }

    public final CreateNewHouseFeature createNewHouseFeature() {
        return DefaultCreateNewHouseFeatureImpl.getInstance();
    }

    public final CancelEventFeature cancelEventFeature() {
        return DefaultCancelEventFeatureImpl.getInstance();
    }
    public final PlannifyEventFeature plannifyEventFeature() {
        return DefaultPlannifyEventFeatureImpl.getInstance();
    }

    
    

}
