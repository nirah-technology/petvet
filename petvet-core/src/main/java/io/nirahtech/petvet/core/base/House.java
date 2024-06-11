package io.nirahtech.petvet.core.base;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.clinic.HealthBook;
import io.nirahtech.petvet.core.planning.Calendar;

public final class House implements Familly {

    private static House instance;
    public static House getInstance() {
        if (Objects.isNull(instance)) {
            instance = new House();
        }
        return instance;
    }

    private HouseIdentifier identifier;
    private String name;
    private Farm farm;
    private final Calendar calendar;
    private final Pharmacy pharmacy;
    private final VegetableGarden garden;
    private Human me;

    private final Set<Human> famillyMembers;

    public House() {
        this.garden = VegetableGarden.getInstance();
        this.pharmacy = Pharmacy.getInstance();
        this.calendar = Calendar.getInstance();
        this.famillyMembers = new HashSet<>();
    }

    public House(
        final String name
    ) {
        this();
        this.name = Objects.requireNonNullElse(name, "Maison");
    }

    public Human getMe() {
        return me;
    }
    public void setMe(Human me) {
        this.me = me;
    }
    public Stream<Human> getFamillyMembers() {
        return this.famillyMembers.stream();
    }

    public void addFamillyMember(final Human human) {
        this.famillyMembers.add(human);
    }

    public Calendar getCalendar() {
        return calendar;
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
    public void setName(String name) {
        this.name = name;
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
