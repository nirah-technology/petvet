package io.nirahtech.petvet.core.contact;

import java.io.Serializable;
import java.util.Objects;

public final class Street implements Serializable {
    private final String type;
    private final String name;

    private Street(
            final String type,
            final String name) {
        this.type = Objects.requireNonNull(type, "Type for street is required.");
        this.name = Objects.requireNonNull(name, "Name for street is required.");
    }

    /**
     * @return the name
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @return the number
     */
    public final String getType() {
        return this.type;
    }

    public static final Street of(
            final String type,
            final String name) {
        return new Street(type, name);
    }
}
