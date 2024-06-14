package io.nirahtech.petvet.features.pharmacy.implementations;

import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.Pharmacy;
import io.nirahtech.petvet.core.pharmacy.Elixir;
import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.pharmacy.DetroyCureFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultDetroyCureFeatureImpl implements DetroyCureFeature {
    private static DetroyCureFeature instance;

    public static final DetroyCureFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Pharmacy pharmacy = Pharmacy.getInstance();
            instance = new DefaultDetroyCureFeatureImpl(pharmacy);
        }
        return instance;
    }

    private final Pharmacy pharmacy;

    private DefaultDetroyCureFeatureImpl(final Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    @Override
    public void detroyCure(Identifier identifierOfTheElixirToDestroy) throws FeatureExecutionException {
        Objects.requireNonNull(identifierOfTheElixirToDestroy, "Identifier of elixir is required for DetroyCureFeature");
        final Optional<Elixir> elixirFound = this.pharmacy.getElixirs()
                .filter(elixir -> elixir.getIdentifier().equals(identifierOfTheElixirToDestroy))
                .findFirst();
        if (!elixirFound.isPresent()) {
            throw new FeatureExecutionException(String.format("Elixir not found: %s", identifierOfTheElixirToDestroy.getId()));
        }
        final Elixir elixir = elixirFound.get();
        this.pharmacy.withdraw(elixir);
    }

}
