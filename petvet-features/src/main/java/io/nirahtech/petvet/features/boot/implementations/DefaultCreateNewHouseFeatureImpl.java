package io.nirahtech.petvet.features.boot.implementations;

import java.util.Objects;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.boot.CreateNewHouseFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultCreateNewHouseFeatureImpl implements CreateNewHouseFeature {

    private static CreateNewHouseFeature instance;

    public static final CreateNewHouseFeature getInstance() {
        if (Objects.isNull(instance)) {
            instance = new DefaultCreateNewHouseFeatureImpl();
        }
        return instance;
    }

    private DefaultCreateNewHouseFeatureImpl() {

    }

    @Override
    public House createNewHouse(String nameOfTheHouse) throws FeatureExecutionException {
        Objects.requireNonNull(nameOfTheHouse, "Name of house is required for CreateNewHouseFeature.");
        final House createdHouse = new House(nameOfTheHouse);
        return createdHouse;
    }
    
}
