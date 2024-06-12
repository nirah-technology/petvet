package io.nirahtech.petvet.features.pets.implementations;

import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.FarmIdentifier;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.pets.RetrieveFarmFeature;

public class DefaultRetrieveFarmFeatureImpl implements RetrieveFarmFeature {
        
    private static RetrieveFarmFeature instance;

    public static final RetrieveFarmFeature getInstance() {
        if (Objects.isNull(instance)) {
            final House house = House.getInstance();
            instance = new DefaultRetrieveFarmFeatureImpl(house);
        }
        return instance;
    }

    private final House house;

    private DefaultRetrieveFarmFeatureImpl(final House house) {
        this.house = house;
    }

    @Override
    public Optional<Farm> retrieveFarm(FarmIdentifier farmIdentifier) {
        return Optional.ofNullable(this.house.getFarm());
    }

}
