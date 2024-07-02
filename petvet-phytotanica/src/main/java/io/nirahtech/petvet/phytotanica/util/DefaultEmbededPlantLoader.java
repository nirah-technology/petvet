package io.nirahtech.petvet.phytotanica.util;

import java.io.File;
import java.net.URL;
import java.time.Month;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import io.nirahtech.petvet.phytotanica.CultivationPeriod;
import io.nirahtech.petvet.phytotanica.Habitat;
import io.nirahtech.petvet.phytotanica.Height;
import io.nirahtech.petvet.phytotanica.Plant;

public final class DefaultEmbededPlantLoader {

    private DefaultEmbededPlantLoader() { }
    
    public static final Optional<Plant> loadPlantFromResourceFile(final String resourceName) {
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

    public static final Set<Plant> loadPlantsFromResources() {
        final Set<Plant> plants = new HashSet<>();

        final String databaseFolder = "databases/plants";
        URL url = DefaultEmbededPlantLoader.class.getClassLoader().getResource(databaseFolder);
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

}
