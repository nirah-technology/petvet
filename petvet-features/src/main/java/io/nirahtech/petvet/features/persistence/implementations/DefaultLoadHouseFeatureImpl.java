package io.nirahtech.petvet.features.persistence.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.persistence.LoadHouseFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultLoadHouseFeatureImpl implements LoadHouseFeature {
    private static volatile LoadHouseFeature instance;

    public static LoadHouseFeature getInstance(final File persistenceFile) {
        if (Objects.isNull(instance)) {
            synchronized (DefaultLoadHouseFeatureImpl.class) {
                if (Objects.isNull(instance)) {
                    instance = new DefaultLoadHouseFeatureImpl(persistenceFile);
                }
            }
        }
        return instance;
    }

    private final File persistenceFile;

    private DefaultLoadHouseFeatureImpl(final File persistenceFile) {
        this.persistenceFile = persistenceFile;
    }

    @Override
    public Optional<House> loadHouse() throws FeatureExecutionException {
        if (!this.persistenceFile.exists()) {
            throw new FeatureExecutionException(String.format("%s doesn't exist.", this.persistenceFile.getAbsolutePath()));
        }
        if (!this.persistenceFile.isFile()) {
            throw new FeatureExecutionException(String.format("%s isn't a valid file.", this.persistenceFile.getAbsolutePath()));
        }

        House house = null;
        try (FileInputStream fileInputStream = new FileInputStream(this.persistenceFile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            house = (House) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new FeatureExecutionException(e);
        }

        return Optional.ofNullable(house);
    }
}
