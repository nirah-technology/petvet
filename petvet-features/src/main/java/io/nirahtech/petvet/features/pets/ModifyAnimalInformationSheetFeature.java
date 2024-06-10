package io.nirahtech.petvet.features.pets;

import java.util.function.Consumer;

import io.nirahtech.petvet.core.base.Pet;

/**
 * 
 * 
 * @author METIVIER Nicolas <nicolas.a.metivier@gmail.com>
 * @since 1.0
 * @serial 202406101537
 * @version 1.0
 * 
 * @see java.util.function.Consumer
 * @see io.nirahtech.petvet.core.base.Pet
 */
@FunctionalInterface
public interface ModifyAnimalInformationSheetFeature extends Consumer<Pet> {
    
}
