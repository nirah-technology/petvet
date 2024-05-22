package io.nirahtech.petvet.core.base;

import java.time.LocalDate;
import java.util.Objects;

import io.nirahtech.petvet.core.animalpark.Animal;

public final class Pet {
    private final Animal animal;
    private final String name;
    private final LocalDate adoptionDate;

    public Pet(
        final Animal animal,
        final String name,
        final LocalDate adoptionDate
    ) {
        this.animal = Objects.requireNonNull(animal, "Animal for pet is required");
        this.name = Objects.requireNonNull(name, "Name for pet is required.");
        this.adoptionDate = Objects.requireNonNull(adoptionDate, "Adoption date for pet is required.");
    }
    /**
     * @return the animal
     */
    public final Animal getAnimal() {
        return this.animal;
    }
    /**
     * @return the adoptionDate
     */
    public final LocalDate getAdoptionDate() {
        return this.adoptionDate;
    }
    /**
     * @return the name
     */
    public final String getName() {
        return this.name;
    }
}
