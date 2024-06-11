package io.nirahtech.petvet.core.base;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.clinic.HealthBook;

public final class House implements Familly {
    private HouseIdentifier identifier;
    private final String name;
    private Farm farm;
    private final Pharmacy pharmacy;
    private final VegetableGarden garden;

    public House(
        final String name
    ) {
        this.name = Objects.requireNonNullElse(name, "Maison");
        this.garden = VegetableGarden.getInstance();
        this.pharmacy = Pharmacy.getInstance();
    }

    public HouseIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(HouseIdentifier identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @return the farm
     */
    public final Optional<Farm> getFarm() {
        return Optional.ofNullable(this.farm);
    }

    public final VegetableGarden getGarden() {
        return this.garden;
    }
    public final Pharmacy getPharmacy() {
        return this.pharmacy;
    }

    /**
     * @param farm the farm to set
     */
    public final void setFarm(final Farm farm) {
        this.farm = farm;
    }

    @Override
    public HealthBook adopt(final Animal animal, final String name, final LocalDate adoptionDate) {
        Objects.requireNonNull(animal, "Animal for adoption is required.");
        Objects.requireNonNull(name, "Animal's name for adoption is required.");
        Objects.requireNonNull(adoptionDate, "Animal's adoption date for adoption is required.");

        if (Objects.isNull(this.farm)) {
            this.farm = new Farm();
        }
        return this.farm.adopt(animal, name, adoptionDate);
    }
}
