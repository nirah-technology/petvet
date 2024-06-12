package io.nirahtech.petvet.features.pets.implementations;

import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.clinic.HealthBook;
import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.pets.RetrieveHealthBookFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRetrieveHealthBookFeatureImpl implements RetrieveHealthBookFeature {
    private static RetrieveHealthBookFeature instance;

    public static final RetrieveHealthBookFeature getInstance() {
        if (Objects.isNull(instance)) {
            final House house = House.getInstance();
            instance = new DefaultRetrieveHealthBookFeatureImpl(house);
        }
        return instance;
    }

    private final House house;

    private DefaultRetrieveHealthBookFeatureImpl(final House house) {
        this.house = house;
    }

    @Override
    public Optional<HealthBook> retrieveHealthBook(Identifier petIdentifier) throws FeatureExecutionException {
        Optional<HealthBook> healthBookFound = Optional.empty();
        final Farm farm = this.house.getFarm();
        healthBookFound = farm.getHealthBooks()
                .filter(healthBook -> healthBook.getPet().getIdentifier().equals(petIdentifier))
                .findFirst();

        return healthBookFound;
    }

}
