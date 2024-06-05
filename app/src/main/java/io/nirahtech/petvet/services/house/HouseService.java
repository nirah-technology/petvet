package io.nirahtech.petvet.services.house;

import java.util.Optional;

import io.nirahtech.petvet.core.base.House;

public interface HouseService {

    /**
     * Load the house
     * @return The loaded house
     */
    void load();

    /**
     * Get the loaded house
     * @return The house
     */
    Optional<House> getHouse();

    /**
     * Save the house
     * @param house The house to save.
     */
    void save(House house);
}
