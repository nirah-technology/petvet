package io.nirahtech.petvet.features.emergency.implementations;

import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.clinic.Vet;
import io.nirahtech.petvet.core.clinic.VetIdentifier;
import io.nirahtech.petvet.features.emergency.RetrieveVetContactDetailFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRetrieveVetContactDetailFeatureImpl implements RetrieveVetContactDetailFeature {
    private static RetrieveVetContactDetailFeature instance;

    public static final RetrieveVetContactDetailFeature getInstance() {
        if (Objects.isNull(instance)) {
            final House house = House.getInstance();
            instance = new DefaultRetrieveVetContactDetailFeatureImpl(house);
        }
        return instance;
    }

    private final House house;

    private DefaultRetrieveVetContactDetailFeatureImpl(final House house) {
        this.house = house;
    }

    @Override
    public Optional<Vet> retrieveEmergencyContactDetail(VetIdentifier vetIdentifier) throws FeatureExecutionException {
        return this.house.getVetDirectory()
                .listAll()
                .filter(vet -> vet.getIdentifier().equals(vetIdentifier))
                .findFirst();
    }
}
