package io.nirahtech.petvet.features.pets.implementations;

import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.base.PetIdentifier;
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
    public Optional<Pet> retrieveAnimalInformationSheet(PetIdentifier petIdentifier) throws FeatureExecutionException {
        Optional<Pet> petFound = Optional.empty();
        if (this.house.getFarm().isPresent()) {
            final Farm farm = this.house.getFarm().get();
            petFound = farm.getPets()
                    .stream()
                    .filter(pet -> pet.getIdentifier().equals(petIdentifier))
                    .findFirst();
        }
        return petFound;
    }
}
