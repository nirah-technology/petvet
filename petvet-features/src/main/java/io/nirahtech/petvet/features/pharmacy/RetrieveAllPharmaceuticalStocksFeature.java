package io.nirahtech.petvet.features.pharmacy;

import java.util.stream.Stream;

import io.nirahtech.petvet.core.clinic.ConventionalMedicine;
import io.nirahtech.petvet.core.clinic.Medication;
import io.nirahtech.petvet.core.clinic.NaturalMedicine;
import io.nirahtech.petvet.core.pharmacy.Elixir;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * 
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101537
 * @version 1.0
 * 
 * @see java.util.function.Consumer
 * @see java.util.stream.Stream
 * @see io.nirahtech.petvet.core.pharmacy.Elixir
 */
public interface RetrieveAllPharmaceuticalStocksFeature {
    Stream<Medication> retrieveAllMedicationStocks() throws FeatureExecutionException;
    Stream<NaturalMedicine> retrieveOnlyAllNaturalMedicinesStocks() throws FeatureExecutionException;
    Stream<ConventionalMedicine> retrieveOnlyAllConventionalMedicinesStocks() throws FeatureExecutionException;
    Stream<Elixir> retrieveAllElixirsStocks() throws FeatureExecutionException;
}
