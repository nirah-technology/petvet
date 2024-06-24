package io.nirahtech.petvet.phytotanica;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.time.Month;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Stream;

public final class VegetableGarden implements Serializable {
    private static VegetableGarden instance;


    private static final Optional<Plant> loadPlantFromResourceFile(final String resourceName) {
        Optional<Plant> loadedPlant = Optional.empty();
        final ResourceBundle bundle = ResourceBundle.getBundle(resourceName);
        if (Objects.nonNull(bundle)) {
            final String scientificName = bundle.getString("plant.scientificName");
            final String commonName = bundle.getString("plant.commonName");
            final String description = bundle.getString("plant.description");
            final CultivationPeriod cultivationPeriod = new CultivationPeriod(
                    Month.valueOf(bundle.getString("plant.cultivationPeriod.start")),
                    Month.valueOf(bundle.getString("plant.cultivationPeriod.end")));
            final Height height = new Height(
                    Integer.parseInt(bundle.getString("plant.height.minInMillimeters")),
                    Integer.parseInt(bundle.getString("plant.height.maxInMillimeters")));
            final Habitat habitat = new Habitat(
                    bundle.getString("plant.habitat.name"),
                    bundle.getString("plant.habitat.description"));
            final String harvest = bundle.getString("plant.harvest");
            final String usage = bundle.getString("plant.usage");
            final File image = new File(bundle.getString("plant.image"));
            final Plant plant = new Plant(scientificName, commonName, description, cultivationPeriod, height, habitat,
                    harvest, usage, image);
            loadedPlant = Optional.of(plant);
        }

        return loadedPlant;
    }

    private static final Set<Plant> loadPlantsFromResources() {
        final Set<Plant> plants = new HashSet<>();

        final String databaseFolder = "databases/plants";
        URL url = VegetableGarden.class.getClassLoader().getResource(databaseFolder);
        if (Objects.nonNull(url)) {
            final File folder = new File(url.getPath());
            if (folder.exists() && folder.isDirectory()) {
                File[] databaseFiles = folder.listFiles();
                if (Objects.nonNull(databaseFiles)) {
                    for (File databaseFile : databaseFiles) {
                        if (databaseFile.isFile()) {
                            if (databaseFile.getName().endsWith(".properties")) {
                                final String resourceNameWithoutExtension = databaseFile.getName().substring(0,
                                        databaseFile.getName().lastIndexOf('.'));

                                final Optional<Plant> plant = loadPlantFromResourceFile(resourceNameWithoutExtension);
                                if (plant.isPresent()) {
                                    plants.add(plant.get());
                                }
                            }   
                        }
                    }
                }
            }
        }

        
        return plants;
    }

    public static final VegetableGarden getInstance() {
        if (instance == null) {
            final Set<Plant> plants = new HashSet<>();
            plants.addAll(loadPlantsFromResources());
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
