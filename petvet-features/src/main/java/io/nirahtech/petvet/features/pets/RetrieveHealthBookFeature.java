package io.nirahtech.petvet.features.pets;

import java.util.function.Consumer;

import io.nirahtech.petvet.core.clinic.HealthBook;

/**
 * 
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101537
 * @version 1.0
 * 
 * @see java.util.function.Consumer
 * @see io.nirahtech.petvet.core.clinic.HealthBook
 */
@FunctionalInterface
public interface RetrieveHealthBookFeature extends Consumer<HealthBook> {
    
}
