package io.nirahtech.petvet.core.base;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.clinic.HealthBook;

public class Farm implements Familly {
    private final Set<Pet> pets = new HashSet<>();
    private final Set<HealthBook> healthBooks = new HashSet<>();

    public Farm(
    ) {
        
    }

    /**
     * @return the pets
     */
    public final Set<Pet> getPets() {
        return Collections.unmodifiableSet(this.pets);
    }

    /**
     * @return the healthBooks
     */
    public final Set<HealthBook> getHealthBooks() {
        return Collections.unmodifiableSet(this.healthBooks);
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
