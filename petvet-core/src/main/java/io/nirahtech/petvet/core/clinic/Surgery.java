package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.time.LocalDate;

public class Surgery implements Serializable {
    private final LocalDate date;
    private final String name;
    private final String description;

    public Surgery(final LocalDate date, final String name, final String description) {
        this.date = date;
        this.name = name;
        this.description = description;
    }
    public final LocalDate getDate() {
        return this.date;
    }
    public final String getName() {
        return this.name;
    }

    public final String getDescription() {
        return this.description;
    }
}
