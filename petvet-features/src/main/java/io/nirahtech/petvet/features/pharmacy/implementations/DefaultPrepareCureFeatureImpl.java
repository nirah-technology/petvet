package io.nirahtech.petvet.features.pharmacy.implementations;

import java.util.Map;
import java.util.Objects;

import io.nirahtech.petvet.core.base.Ingredient;
import io.nirahtech.petvet.core.base.Pharmacy;
import io.nirahtech.petvet.core.pharmacy.Elixir;
import io.nirahtech.petvet.core.util.Volume;
import io.nirahtech.petvet.features.pharmacy.PrepareCureFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultPrepareCureFeatureImpl implements PrepareCureFeature {
    private static PrepareCureFeature instance;

    public static final PrepareCureFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Pharmacy pharmacy = Pharmacy.getInstance();
            instance = new DefaultPrepareCureFeatureImpl(pharmacy);
        }
        return instance;
    }

    private final Pharmacy pharmacy;

    private DefaultPrepareCureFeatureImpl(final Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    @Override
    public Elixir prepareCure(String name, Map<Ingredient, Volume> composition) throws FeatureExecutionException {
        final Elixir elixir = new Elixir(name, composition);
        this.pharmacy.supply(elixir);
        return elixir;
    }

    
}
