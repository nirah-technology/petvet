package io.nirahtech.petvet.persistance.databases.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Database {
    private final String name;
    protected final Map<String, Table<?>> tables;

    protected Database(final String name, final Set<Table<?>> tables) {
        this.name = name;
        this.tables = new HashMap<>();
        tables.forEach(table -> this.tables.put(table.getName(), table));
    }

    public void clear() {
        this.tables.values().forEach(Table::clear);
    }

    public final String getName() {
        return this.name;
    }

    public final Collection<Table<?>> getTables() {
        return Collections.unmodifiableCollection(this.tables.values());
    }

    public static final class Factory {
        private Factory() { }

        public static Database create(final String name, final Set<Table<?>> tables) {
            return new Database(name, tables);
        }
    }
}
