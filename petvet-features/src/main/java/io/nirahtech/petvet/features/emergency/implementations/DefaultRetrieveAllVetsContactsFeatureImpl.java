package io.nirahtech.petvet.features.emergency.implementations;

import java.util.Objects;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.clinic.Vet;
import io.nirahtech.petvet.features.emergency.RetrieveAllVetsContactsFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRetrieveAllVetsContactsFeatureImpl implements RetrieveAllVetsContactsFeature {
    private static RetrieveAllVetsContactsFeature instance;

    public static final RetrieveAllVetsContactsFeature getInstance() {
        if (Objects.isNull(instance)) {
            final House house = House.getInstance();
            instance = new DefaultRetrieveAllVetsContactsFeatureImpl(house);
        }
        return instance;
    }

    private final House house;

    private DefaultRetrieveAllVetsContactsFeatureImpl(final House house) {
        this.house = house;
    }

    @Override
    public Stream<Vet> retrieveAllEmergencyContacts() throws FeatureExecutionException {
        return this.house.getVetDirectory().listAll();
    }
}
