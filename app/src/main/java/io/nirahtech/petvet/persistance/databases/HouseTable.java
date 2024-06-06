package io.nirahtech.petvet.persistance.databases;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.persistance.databases.core.Column;
import io.nirahtech.petvet.persistance.databases.core.Record;
import io.nirahtech.petvet.persistance.databases.core.Table;

public final class HouseTable extends Table<House> {
    public static final String TABLE_NAME = House.class.getSimpleName().toLowerCase();

    private HouseTable(Set<Column> columns) {
        super(TABLE_NAME, columns);
    }

    public Stream<House> selectAllHouses() {
        return null;
    }

    public void insert(House house) {

    }

    public void delete(House house) {

    }

    public void update(House house) {

    }

    public static HouseTable create() {
        final Set<Column> columns = new HashSet<>();
        final Column name = Column.Factory.create(true, "name", "TEXT");
        columns.add(name);
        return new HouseTable(columns);
    }
}
