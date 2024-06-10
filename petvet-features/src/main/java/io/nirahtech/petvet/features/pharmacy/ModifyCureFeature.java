package io.nirahtech.petvet.features.pharmacy;

import java.util.function.Consumer;

import io.nirahtech.petvet.core.pharmacy.Elixir;


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
public interface ModifyCureFeature extends Consumer<Elixir> {
    
}
