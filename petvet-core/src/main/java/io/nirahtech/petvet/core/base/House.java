package io.nirahtech.petvet.core.base;

import java.util.Objects;
import java.util.Optional;

public final class House {
    private final String name;
    private Farm farm;

    public House(
        final String name
    ) {
        this.name = Objects.requireNonNullElse(name, "Maison");
    }

    /**
     * @return the name
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @return the farm
     */
    public final Optional<Farm> getFarm() {
        return Optional.ofNullable(this.farm);
    }

    /**
     * @param farm the farm to set
     */
    public final void setFarm(final Farm farm) {
        this.farm = farm;
    }
}
