package io.nirahtech.petvet.features;

import java.io.File;
import java.util.Optional;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class Test {
    
    public static void main(String[] args) throws FeatureExecutionException {
        final File file = new File("TEST-TEST", "test.test");
        FeaturesRegistry features = FeaturesRegistry.getInstance();
        final House house = features.createNewHouseFeature().createNewHouse("test");
        System.out.println(house);
        features.saveHouseFeature(file).saveHouse(house);
        final Optional<House> loadedHouse = features.loadHouseFeature(file).loadHouse();
        System.out.println(loadedHouse);

    }
}
