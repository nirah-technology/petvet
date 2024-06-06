package io.nirahtech.petvet.persistance.databases.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Table<T> {
    private final String name;
    private final Set<Column> columns;
    private final List<Record> records;

    protected Table(final String name, final Set<Column> columns) {
        this.name = name;
        this.columns = columns;
        this.records = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public Stream<Record> selectAll() {
        return records.stream();
    }

    public void insert(Map<String, Object> objectAsMap) {
        if (objectAsMap.size() != this.columns.size()) {
            throw new RuntimeException("Properties count of object is not matching with columns count in table.");
        }
        boolean isTableNamePresent = true;
        for (String propertyNameInObject : objectAsMap.keySet()) {
            isTableNamePresent = this.columns
                    .stream()
                    .map(Column::getName)
                    .filter(columneName -> columneName.equalsIgnoreCase(propertyNameInObject))
                    .findAny()
                    .isPresent();
            if (!isTableNamePresent) {
                break;
            }
        }
        // if ()

        final Map<Column, Object> recordMap = new HashMap<>();


        this.records.add(Record.Factory.create(recordMap));
    }

    public void clear() {
        this.records.clear();
    }

    public static final class Factory {
        private  Factory() { }

        public static Table<?> create(final String name, Column... columns) {
            Objects.requireNonNull(name, "Name is required for table.");
            Objects.requireNonNull(columns, "Column is required for table.");
            final Set<Column> columnsSet = Stream.of(columns).collect(Collectors.toSet());
            return new Table(name, columnsSet);
        }
    }
}
