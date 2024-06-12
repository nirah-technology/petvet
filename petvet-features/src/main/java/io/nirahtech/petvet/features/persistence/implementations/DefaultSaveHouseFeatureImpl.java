package io.nirahtech.petvet.features.persistence.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.persistence.SaveHouseFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultSaveHouseFeatureImpl implements SaveHouseFeature {
    private static volatile SaveHouseFeature instance;

    public static SaveHouseFeature getInstance(final File persistenceFile) {
        if (Objects.isNull(instance)) {
            synchronized (DefaultSaveHouseFeatureImpl.class) {
                if (Objects.isNull(instance)) {
                    instance = new DefaultSaveHouseFeatureImpl(persistenceFile);
                }
            }
        }
        return instance;
    }

    private final File persistenceFile;

    private DefaultSaveHouseFeatureImpl(final File persistenceFile) {
        this.persistenceFile = persistenceFile;
    }

    @Override
    public void saveHouse(House house) throws FeatureExecutionException {
        if (Objects.isNull(house)) {
            throw new FeatureExecutionException("House cannot be null.");
        }

        final File folder = this.persistenceFile.getParentFile();
        if (!Objects.requireNonNull(folder).exists()) {
            folder.mkdirs();
        }
        if (!this.persistenceFile.exists()) {
            try {
                this.persistenceFile.createNewFile();
            } catch (IOException e) {
                throw new FeatureExecutionException(e);
            }
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(this.persistenceFile);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(house);
        } catch (IOException e) {
            throw new FeatureExecutionException(e);
        }
    }
}
