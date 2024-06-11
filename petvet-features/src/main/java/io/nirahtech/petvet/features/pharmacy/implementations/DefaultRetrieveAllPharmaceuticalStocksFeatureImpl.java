package io.nirahtech.petvet.features.pharmacy.implementations;

import java.util.Objects;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.base.Pharmacy;
import io.nirahtech.petvet.core.clinic.ConventionalMedicine;
import io.nirahtech.petvet.core.clinic.Medication;
import io.nirahtech.petvet.core.clinic.NaturalMedicine;
import io.nirahtech.petvet.core.pharmacy.Elixir;
import io.nirahtech.petvet.features.pharmacy.RetrieveAllPharmaceuticalStocksFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRetrieveAllPharmaceuticalStocksFeatureImpl implements RetrieveAllPharmaceuticalStocksFeature {
private static RetrieveAllPharmaceuticalStocksFeature instance;

    public static final RetrieveAllPharmaceuticalStocksFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Pharmacy pharmacy = Pharmacy.getInstance();
            instance = new DefaultRetrieveAllPharmaceuticalStocksFeatureImpl(pharmacy);
        }
        return instance;
    }

    private final Pharmacy pharmacy;

    private DefaultRetrieveAllPharmaceuticalStocksFeatureImpl(final Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    @Override
    public Stream<Medication> retrieveAllMedicationStocks() throws FeatureExecutionException {
        return this.pharmacy.getAllMedications();
    }

    @Override
    public Stream<NaturalMedicine> retrieveOnlyAllNaturalMedicinesStocks() throws FeatureExecutionException {
        return this.pharmacy.getOnlyAllNatualMedecines();
    }

    @Override
    public Stream<ConventionalMedicine> retrieveOnlyAllConventionalMedicinesStocks() throws FeatureExecutionException {
        return this.pharmacy.getOnlyAllConventionalMedicines();
    }

    @Override
    public Stream<Elixir> retrieveAllElixirsStocks() throws FeatureExecutionException {
        return this.pharmacy.getElixirs();
    }
    
}
