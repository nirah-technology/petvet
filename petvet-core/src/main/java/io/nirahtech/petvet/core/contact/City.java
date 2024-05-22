package io.nirahtech.petvet.core.contact;

import java.util.Objects;

public final class City {
    private final int zipCode;
    private final String name;

    private City(
        final int zipCode,
        final String name
    ) {
        this.zipCode = zipCode;
        this.name = Objects.requireNonNull(name, "City name is required.");
    }
    /**
     * @return the zipCode
     */
    public final int getZipCode() {
        return this.zipCode;
    }
    /**
     * @return the name
     */
    public final String getName() {
        return this.name;
    }

    public static final City of(
        final int zipCode,
        final String name) {
            return new City(zipCode, name);
        }
}
