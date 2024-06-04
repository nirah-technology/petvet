package io.nirahtech.petvet.services.storage.house;

import io.nirahtech.petvet.core.base.House;

public interface HouseService {

    /**
     * Load the house
     * @return The loaded house
     */
    House load();

    /**
     * Get the loaded house
     * @return The house
     */
    House get();

    /**
     * Save the house
     * @param house The house to save.
     */
    void save(House house);
}
