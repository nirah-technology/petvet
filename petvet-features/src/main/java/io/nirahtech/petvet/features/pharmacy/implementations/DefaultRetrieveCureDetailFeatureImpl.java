package io.nirahtech.petvet.features.pharmacy.implementations;

import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.Pharmacy;
import io.nirahtech.petvet.core.pharmacy.Elixir;
import io.nirahtech.petvet.core.pharmacy.ElixirIdentifier;
import io.nirahtech.petvet.features.pharmacy.RetrieveCureDetailFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRetrieveCureDetailFeatureImpl implements RetrieveCureDetailFeature {
    private static RetrieveCureDetailFeature instance;

    public static final RetrieveCureDetailFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Pharmacy pharmacy = Pharmacy.getInstance();
            instance = new DefaultRetrieveCureDetailFeatureImpl(pharmacy);
        }
        return instance;
    }

    private final Pharmacy pharmacy;

    private DefaultRetrieveCureDetailFeatureImpl(final Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    @Override
    public Optional<Elixir> retriveCureDetail(ElixirIdentifier elixirIdentifier) throws FeatureExecutionException {
        Objects.requireNonNull(elixirIdentifier, "Elixir identifier is required for RetrieveCureDetailFeature.");
        return this.pharmacy.getElixirs()
        .filter(elixir -> elixir.getIdentifier().equals(elixirIdentifier))
        .findFirst();
    }

}
