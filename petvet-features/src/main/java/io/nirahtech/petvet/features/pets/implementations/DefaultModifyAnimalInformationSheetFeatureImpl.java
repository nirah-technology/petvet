package io.nirahtech.petvet.features.pets.implementations;

import java.io.File;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.animalpark.Breed;
import io.nirahtech.petvet.core.animalpark.Gender;
import io.nirahtech.petvet.core.animalpark.Species;
import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.base.Microship;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.util.Weight;
import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.pets.ModifyAnimalInformationSheetFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultModifyAnimalInformationSheetFeatureImpl implements ModifyAnimalInformationSheetFeature {
    private static ModifyAnimalInformationSheetFeature instance;

    public static final ModifyAnimalInformationSheetFeature getInstance() {
        if (Objects.isNull(instance)) {
            final House house = House.getInstance();
            instance = new DefaultModifyAnimalInformationSheetFeatureImpl(house);
        }
        return instance;
    }

    private final House house;

    private DefaultModifyAnimalInformationSheetFeatureImpl(final House house) {
        this.house = house;

    }

    @Override
    public Pet modifyAnimalInformationSheet(Identifier petIdentifier, Species species, Breed breed, Gender gender,
            LocalDate birthDate, LocalDate deathDate, Weight weight, File picture, String name, LocalDate adoptionDate,
            Boolean isTatooed, Microship microship) throws FeatureExecutionException {
        Objects.requireNonNull(petIdentifier, "Pet identifier is required for ModifyAnimalInformationSheetFeature");
        Optional<Pet> petFound = Optional.empty();
        final Farm farm = this.house.getFarm();
        petFound = farm.getPets()
                .filter(pet -> pet.getIdentifier().equals(petIdentifier))
                .findFirst();
        if (!petFound.isPresent()) {
            throw new FeatureExecutionException("Pet not found");
        }
        final Pet pet = petFound.get();

        if (Objects.nonNull(species)) {
            pet.getAnimal().setSpecies(species);
        }

        if (Objects.nonNull(breed)) {
            pet.getAnimal().setBreed(breed);
        }
        if (Objects.nonNull(gender)) {
            pet.getAnimal().setGender(gender);
        }

        if (Objects.nonNull(birthDate)) {
            pet.getAnimal().birth(birthDate);
        }
        if (Objects.nonNull(deathDate)) {
            pet.getAnimal().death(deathDate);
        }
        if (Objects.nonNull(weight)) {
            pet.getAnimal().setWeight(weight);
        }
        if (Objects.nonNull(picture)) {
            pet.getAnimal().setPicture(picture);
        }
        if (Objects.nonNull(name)) {
            pet.setName(name);
        }
        if (Objects.nonNull(adoptionDate)) {
            pet.setAdoptionDate(adoptionDate);
        }
        if (Objects.nonNull(isTatooed)) {
            pet.setTatooed(isTatooed);
        }
        if (Objects.nonNull(microship)) {
            pet.setMicroship(microship);
        }

        return pet;
    }

}
