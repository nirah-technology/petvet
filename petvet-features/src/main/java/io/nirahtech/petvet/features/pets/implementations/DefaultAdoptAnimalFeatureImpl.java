package io.nirahtech.petvet.features.pets.implementations;

import java.time.LocalDate;
import java.util.Objects;

import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.animalpark.AnimalIdentifier;
import io.nirahtech.petvet.core.animalpark.Breed;
import io.nirahtech.petvet.core.animalpark.Gender;
import io.nirahtech.petvet.core.animalpark.Species;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.base.PetIdentifier;
import io.nirahtech.petvet.core.clinic.HealthBook;
import io.nirahtech.petvet.core.clinic.HealthBookIdentifier;
import io.nirahtech.petvet.features.pets.AdoptAnimalFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultAdoptAnimalFeatureImpl implements AdoptAnimalFeature {
    
    private static AdoptAnimalFeature instance;

    public static final AdoptAnimalFeature getInstance() {
        if (Objects.isNull(instance)) {
            final House house = House.getInstance();
            instance = new DefaultAdoptAnimalFeatureImpl(house);
        }
        return instance;
    }

    private final House house;

    private DefaultAdoptAnimalFeatureImpl(final House house) {
        this.house = house;
    }

    @Override
    public HealthBook adoptAnimal(Species species, Breed breed, Gender gender, LocalDate birthDate, String name,
            LocalDate adoptionDate) throws FeatureExecutionException {
        final Animal animal = new Animal();
        animal.setIdentifier(AnimalIdentifier.generate());
        animal.setSpecies(species);
        animal.setBreed(breed);
        animal.setGender(gender);
        animal.birth(birthDate);
        final HealthBook healthBook = this.house.adopt(animal, name, adoptionDate);
        healthBook.setIdentifier(HealthBookIdentifier.generate());
        final Pet pet = healthBook.getPet();
        pet.setIdentifier(PetIdentifier.generate());
        return healthBook;

    }

}
