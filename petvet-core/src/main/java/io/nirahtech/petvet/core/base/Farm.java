package io.nirahtech.petvet.core.base;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.nirahtech.petvet.core.clinic.HealthBook;

public class Farm {
    private final Set<Pet> pets;
    private final Set<HealthBook> healthBooks;

    public Farm(
    ) {
        this.pets = new HashSet<>();
        this.healthBooks = new HashSet<>();
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
}
