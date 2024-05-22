package io.nirahtech.petvet.core.clinic;

import java.util.Optional;

import io.nirahtech.petvet.core.pharmacy.Elixir;

public final class NaturalMedicine extends Medication {
    private final Elixir elixir;

    public NaturalMedicine(
        final String name,
        final Prescription prescription) {
            this(name, prescription, null);
    }

    public NaturalMedicine(
        
        final String name,
        final Prescription prescription,
        final Elixir elixir) {
        super(name, prescription);
        this.elixir = elixir;
    }

    public final Optional<Elixir> getElixir() {
        return Optional.ofNullable(elixir);
    }
}
