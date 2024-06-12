package io.nirahtech.petvet.core.base;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.clinic.HealthBook;
import io.nirahtech.petvet.core.util.identifier.Identifier;

public class Farm implements AbleToAdopt {
    private Identifier identifier;
    private final Set<Pet> pets = new HashSet<>();
    private final Set<HealthBook> healthBooks = new HashSet<>();

    public Farm(
    ) {
        
    }

    public Identifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the pets
     */
    public final Stream<Pet> getPets() {
        return this.pets.stream();
    }

    /**
     * @return the healthBooks
     */
    public final Stream<HealthBook> getHealthBooks() {
        return this.healthBooks.stream();
    }

    @Override
    public HealthBook adopt(final Animal animal, final String name, final LocalDate adoptionDate) {
        Objects.requireNonNull(animal, "Animal for adoption is required.");
        Objects.requireNonNull(name, "Animal's name for adoption is required.");
        Objects.requireNonNull(adoptionDate, "Animal's adoption date for adoption is required.");

        HealthBook healthBook;
        final Pet pet = new Pet(animal, name, adoptionDate);
        if (!this.pets.contains(pet)) {
            this.pets.add(pet);
            healthBook = new HealthBook(pet);
            this.healthBooks.add(healthBook);
        } else {
            final Optional<HealthBook> healthBookFound = this.healthBooks
                    .stream()
                    .filter(book -> book.getPet().equals(pet))
                    .findFirst();
            if (healthBookFound.isPresent()) {
                healthBook = healthBookFound.get();
            } else {
                healthBook = new HealthBook(pet);
                this.healthBooks.add(healthBook);
            }
        }

        return healthBook;
    }


}
