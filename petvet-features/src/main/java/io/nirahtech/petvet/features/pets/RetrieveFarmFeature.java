package io.nirahtech.petvet.features.pets;

import java.util.Optional;

import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.FarmIdentifier;

/**
 * 
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101537
 * @version 1.0
 * 
 * @see java.util.function.Consumer
 * @see io.nirahtech.petvet.core.base.Farm
 */
@FunctionalInterface
public interface RetrieveFarmFeature {
    Optional<Farm> retrieveFarm(final FarmIdentifier farmIdentifier);
}
