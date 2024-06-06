package io.nirahtech.petvet.persistance.databases;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.persistance.databases.core.Database;
import io.nirahtech.petvet.persistance.databases.core.Table;

public final class PetVetDatabase extends Database {
    private static final String DATABASE_NAME = "petvet";

    private static PetVetDatabase instance = null;

    public static PetVetDatabase getInstance() {
        if (Objects.isNull(PetVetDatabase.instance)) {
            PetVetDatabase.instance = PetVetDatabase.create();
        }
        return PetVetDatabase.instance;
    }

    private PetVetDatabase(String name, Set<Table<?>> tables) {
        super(name, tables);
    }

    public HouseTable getHouseTable() {
        return (HouseTable) super.tables.get(HouseTable.TABLE_NAME);
    }


    private static PetVetDatabase create() {
        final HouseTable houseTable = HouseTable.create();
        final Set<Table<?>> tables = Stream.of(houseTable).collect(Collectors.toSet());
        return new PetVetDatabase(DATABASE_NAME, tables);
    }
}
