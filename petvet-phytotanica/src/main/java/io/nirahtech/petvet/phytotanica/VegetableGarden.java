package io.nirahtech.petvet.phytotanica;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public final class VegetableGarden implements Serializable {
    private static VegetableGarden instance;


    public static final VegetableGarden getInstance() {
        if (instance == null) {
            final Set<Plant> plants = new HashSet<>();
            instance = new VegetableGarden(plants);
        }
        return instance;
    }
    private final Set<Plant> plants;

    private VegetableGarden(final Set<Plant> plants) {
        this.plants = new HashSet<>();
        if (Objects.nonNull(plants)) {
            this.plants.addAll(plants);
        }
    }

    public final Stream<Plant> getPlants() {
        return this.plants.stream();
    }

    public final void addPlant(final Plant plant) {
        if (Objects.nonNull(plant)) {
            this.plants.add(plant);
        }
    }

    public final void removePlant(final Plant plant) {
        if (Objects.nonNull(plant)) {
            this.plants.remove(plant);
        }
    }

    public final void clear() {
        this.plants.clear();
    }

    public final boolean isEmpty() {
        return this.plants.isEmpty();
    }

    public final int size() {
        return this.plants.size();
    }

    public final boolean contains(final Plant plant) {
        Objects.requireNonNull(plant);
        return this.plants.contains(plant);
    }

    public final boolean containsAll(final Collection<Plant> plants) {
        Objects.requireNonNull(plants);
        return this.plants.containsAll(plants);
    }

}
