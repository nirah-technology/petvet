package io.nirahtech.petvet.ai.classifiers.classification;

import java.util.Optional;

public final class AnimalClassification implements Classification {
    private final String species;
    private final String breed;
    
    public AnimalClassification(
        final String species
    ) {
        this(species, null);
    }
    public AnimalClassification(
        final String species,
        final String breed
    ) {
        this.species = species;
        this.breed = breed;
    }
    public final String getSpecies() {
        return this.species;
    }
    public final Optional<String> getBreed() {
        return Optional.ofNullable(this.breed);
    }

}
