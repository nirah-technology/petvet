package io.nirahtech.petvet.features.emergency.implementations;

import java.util.Objects;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.clinic.VetIdentifier;
import io.nirahtech.petvet.features.emergency.DeleteVetFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultDeleteVetFeatureImpl implements DeleteVetFeature {
    private static DeleteVetFeature instance;

    public static final DeleteVetFeature getInstance() {
        if (Objects.isNull(instance)) {
            final House house = House.getInstance();
            instance = new DefaultDeleteVetFeatureImpl(house);
        }
        return instance;
    }

    private final House house;

    private DefaultDeleteVetFeatureImpl(final House house) {
        this.house = house;
    }

    @Override
    public void deleteVetContact(VetIdentifier vetIdentifierToDelete) throws FeatureExecutionException {
        Objects.requireNonNull(vetIdentifierToDelete, "Vet is required for DeleteVetFeature");
        this.house.getVetDirectory()
                .listAll()
                .filter(vet -> vet.getIdentifier().equals(vetIdentifierToDelete))
                .findFirst()
                .ifPresent(vetToRemove -> {
                    this.house.getVetDirectory().remove(vetToRemove);
                });
    }

}
