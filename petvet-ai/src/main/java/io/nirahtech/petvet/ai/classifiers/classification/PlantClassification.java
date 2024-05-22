package io.nirahtech.petvet.ai.classifiers.classification;

public class PlantClassification implements Classification {
    private final String species;
    
    public PlantClassification(
        final String species
    ) {
        this.species = species;
    }

    public final String getSpecies() {
        return this.species;
    }
}
