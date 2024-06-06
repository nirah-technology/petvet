package io.nirahtech.petvet.persistance.databases.core;

import java.util.Objects;

public final class Column {
    private final boolean isPrimaryKey;
    private final String name;
    private final String type;

    private final boolean isUnique;

    private final Table<?> foreignTable;
    private final Column foreignColumn;
    private final String foreignKey;

    private Column(boolean isPrimaryKey, String name, String type, boolean isUnique, Table<?> foreignTable, Column foreignColumn, String foreignKey) {
        this.isPrimaryKey = isPrimaryKey;
        this.name = name;
        this.type = type;
        this.isUnique = isUnique;
        this.foreignTable = foreignTable;
        this.foreignColumn = foreignColumn;
        this.foreignKey = foreignKey;
    }

    public boolean isPrimaryKey() {
        return this.isPrimaryKey;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public boolean isUnique() {
        return this.isUnique;
    }

    public Table<?> getForeignTable() {
        return this.foreignTable;
    }

    public Column getForeignColumn() {
        return this.foreignColumn;
    }

    public String getForeignKey() {
        return this.foreignKey;
    }

    public static final class Factory {
        private Factory() { }

        public static Column create(String name, String type) {
            Objects.requireNonNull(name, "Name is required for column creation");
            Objects.requireNonNull(type, "Type is required for column creation");
            return new Column(false, name, type, false, null, null, null);
        }
        public static Column create(boolean isPrimaryKey, String name, String type) {
            Objects.requireNonNull(name, "Name is required for column creation");
            Objects.requireNonNull(type, "Type is required for column creation");
            return new Column(isPrimaryKey, name, type, false, null, null, null);
        }

        public static Column create(String name, String type, boolean isUnique) {
            Objects.requireNonNull(name, "Name is required for column creation");
            Objects.requireNonNull(type, "Type is required for column creation");
            return new Column(false, name, type, isUnique, null, null, null);
        }
        public static Column create(boolean isPrimaryKey, String name, String type, boolean isUnique) {
            Objects.requireNonNull(name, "Name is required for column creation");
            Objects.requireNonNull(type, "Type is required for column creation");
            return new Column(isPrimaryKey, name, type, isUnique, null, null, null);
        }

        public static Column create(String name, String type, Table<?> foreignTable, Column foreignColumn, String foreignKey) {
            Objects.requireNonNull(name, "Name is required for column creation");
            Objects.requireNonNull(type, "Type is required for column creation");
            Objects.requireNonNull(type, "Foreign Table is required for column creation");
            Objects.requireNonNull(type, "Foreign Column is required for column creation");
            Objects.requireNonNull(type, "Foreign Key is required for column creation");
            return new Column(false, name, type, false, foreignTable, foreignColumn, foreignKey);
        }
        public static Column create(boolean isPrimaryKey, String name, String type, Table<?> foreignTable, Column foreignColumn, String foreignKey) {
            Objects.requireNonNull(name, "Name is required for column creation");
            Objects.requireNonNull(type, "Type is required for column creation");
            Objects.requireNonNull(type, "Foreign Table is required for column creation");
            Objects.requireNonNull(type, "Foreign Column is required for column creation");
            Objects.requireNonNull(type, "Foreign Key is required for column creation");
            return new Column(isPrimaryKey, name, type, false, foreignTable, foreignColumn, foreignKey);
        }

        public static Column create(String name, String type, boolean isUnique, Table<?> foreignTable, Column foreignColumn, String foreignKey) {
            Objects.requireNonNull(name, "Name is required for column creation");
            Objects.requireNonNull(type, "Type is required for column creation");
            Objects.requireNonNull(type, "Foreign Table is required for column creation");
            Objects.requireNonNull(type, "Foreign Column is required for column creation");
            Objects.requireNonNull(type, "Foreign Key is required for column creation");
            return new Column(false, name, type, isUnique, foreignTable, foreignColumn, foreignKey);
        }
        public static Column create(boolean isPrimaryKey, String name, String type, boolean isUnique, Table<?> foreignTable, Column foreignColumn, String foreignKey) {
            Objects.requireNonNull(name, "Name is required for column creation");
            Objects.requireNonNull(type, "Type is required for column creation");
            Objects.requireNonNull(type, "Foreign Table is required for column creation");
            Objects.requireNonNull(type, "Foreign Column is required for column creation");
            Objects.requireNonNull(type, "Foreign Key is required for column creation");
            return new Column(isPrimaryKey, name, type, isUnique, foreignTable, foreignColumn, foreignKey);
        }

    }
}
