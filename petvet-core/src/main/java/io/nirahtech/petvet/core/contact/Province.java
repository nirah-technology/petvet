package io.nirahtech.petvet.core.contact;

import java.util.Objects;

public class Province {
    private final int departmentNumber;
    private final String name;

    private Province(
            final int departmentNumber,
            final String name) {
        this.departmentNumber = departmentNumber;
        this.name = Objects.requireNonNull(name, "Name for province is required.");
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
    public final int getDepartmentNumber() {
        return this.departmentNumber;
    }

    public static final Province of(
            final int departmentNumber,
            final String name) {
        return new Province(departmentNumber, name);
    }
}
