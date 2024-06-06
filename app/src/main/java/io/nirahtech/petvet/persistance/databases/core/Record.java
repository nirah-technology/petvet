package io.nirahtech.petvet.persistance.databases.core;

import java.util.Map;
import java.util.Objects;

public class Record {
    private final Map<Column, Object> fields;

    private Record(final Map<Column, Object> fields) {
        this.fields = fields;
    }

    public static final class Factory {
        private Factory() { }

        public static Record create(final Map<Column, Object> fields) {
            Objects.requireNonNull(fields, "Fields are required for record creation.");
            return new Record(fields);
        }
    }
}
