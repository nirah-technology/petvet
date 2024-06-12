package io.nirahtech.petvet.features.pharmacy;

import java.util.Optional;

import io.nirahtech.petvet.core.pharmacy.Elixir;
import io.nirahtech.petvet.core.util.identifier.Identifier;
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
 * @see io.nirahtech.petvet.core.pharmacy.Elixir
 */
@FunctionalInterface
public interface RetrieveCureDetailFeature {
    Optional<Elixir> retriveCureDetail(final Identifier elixirIdentifier) throws FeatureExecutionException;
}
