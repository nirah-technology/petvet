package io.nirahtech.petvet.services.storage.house;

import java.util.Objects;

import io.nirahtech.petvet.core.base.House;

public final class HouseServiceImpl implements HouseService {

    private static HouseService instance;

    public static HouseService getInstance() {
        if (Objects.isNull(HouseServiceImpl.instance)) {
            HouseServiceImpl.instance = new HouseServiceImpl();
        }
        return HouseServiceImpl.instance;
    }

    private House loadedHouse;

    private HouseServiceImpl() {
        this.load();
    }

    @Override
    public House load() {
        return this.get();
    }

    @Override
    public House get() {
        return this.loadedHouse;
    }

    @Override
    public void save(House house) {
        this.loadedHouse = house;
    }
}
