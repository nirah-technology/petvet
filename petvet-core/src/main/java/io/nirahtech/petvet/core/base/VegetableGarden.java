package io.nirahtech.petvet.core.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public final class VegetableGarden implements Serializable {

    private final Set<Plant> plants;
    
    public VegetableGarden() {
        this.plants = new HashSet<>();
    }

    public Stream<Plant> getPlants() {
        return this.plants.stream();
    }

    public final void grow(final Plant plant) {
        this.plants.add(plant);
    }

    public final void clear() {
        this.plants.clear();
    }

    public final void abandoningCultivationOf(final Plant plant) {
        this.plants.remove(plant);
    }
    
}
