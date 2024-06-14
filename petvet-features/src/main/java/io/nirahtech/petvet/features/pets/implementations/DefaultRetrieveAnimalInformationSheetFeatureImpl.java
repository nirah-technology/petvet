package io.nirahtech.petvet.features.pets.implementations;

import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.pets.RetrieveAnimalInformationSheetFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRetrieveAnimalInformationSheetFeatureImpl implements RetrieveAnimalInformationSheetFeature {
    private static RetrieveAnimalInformationSheetFeature instance;

    public static final RetrieveAnimalInformationSheetFeature getInstance() {
        if (Objects.isNull(instance)) {
            final House house = House.getInstance();
            instance = new DefaultRetrieveAnimalInformationSheetFeatureImpl(house);
        }
        return instance;
    }

    private final House house;

    private DefaultRetrieveAnimalInformationSheetFeatureImpl(final House house) {
        this.house = house;
    }

    @Override
    public Optional<Pet> retrieveAnimalInformationSheet(Identifier petIdentifier) throws FeatureExecutionException {
        Optional<Pet> petFound = Optional.empty();
        final Farm farm = this.house.getFarm();
        petFound = farm.getPets()
                .filter(pet -> pet.getIdentifier().equals(petIdentifier))
                .findFirst();
        return petFound;
    }
}
