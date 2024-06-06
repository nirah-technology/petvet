package io.nirahtech.petvet.services.house;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import io.nirahtech.petvet.core.animalpark.Species;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.persistance.databases.HouseTable;
import io.nirahtech.petvet.persistance.databases.PetVetDatabase;
import io.nirahtech.petvet.persistance.databases.core.Column;
import io.nirahtech.petvet.persistance.databases.core.Database;
import io.nirahtech.petvet.persistance.databases.core.Table;
import io.nirahtech.petvet.services.storage.LocalStorageService;
import io.nirahtech.petvet.services.storage.StorageService;

public final class HouseServiceImpl implements HouseService {

    private static final String DATABASE_FILE_NAME = "house.db";


    private static HouseService instance;

    public static HouseService getInstance(final Context context) {
        if (Objects.isNull(HouseServiceImpl.instance)) {
            HouseServiceImpl.instance = new HouseServiceImpl(context);
        }
        return HouseServiceImpl.instance;
    }

    private House loadedHouse;
    private final StorageService storageService;
    // private final PetVetDatabase database;
    private final File databaseFile;


    private HouseServiceImpl(final Context context) {
        this.storageService = new LocalStorageService();
        this.databaseFile = new File(this.storageService.getMediaStore(context), DATABASE_FILE_NAME);
        System.out.println(this.databaseFile);
        this.load();

        final Set<Table<?>> tables = new HashSet<>();

        // this.database = PetVetDatabase.getInstance();
        // this.database.clear();
        // this.database.getHouseTable().selectAllHouses();
        // this.database.getHouseTable()

    }

    @Override
    public void load() {
        if (this.storageService.exists(databaseFile)) {
            try {
                this.loadedHouse = this.storageService.load(databaseFile);
                System.out.println(this.loadedHouse.toString());
                System.out.println("***************");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("DB not exists");
        }
    }

    @Override
    public Optional<House> getHouse() {
        return Optional.ofNullable(this.loadedHouse);
    }

    @Override
    public void save(House house) {
        this.loadedHouse = house;
        try {
            this.storageService.save(this.loadedHouse, this.databaseFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
