package io.nirahtech.petvet.core.base;

import java.time.LocalDate;
import java.util.Objects;

import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.clinic.HealthBook;
import io.nirahtech.petvet.core.planning.Calendar;

public final class House implements AbleToAdopt {

    private static House instance;
    public static House getInstance() {
        if (Objects.isNull(instance)) {
            instance = new House();
        }
        return instance;
    }

    private HouseIdentifier identifier;
    private String name;
    private final Family family;
    private final Calendar calendar;
    private final VegetableGarden garden;
    private final Farm farm;
    private final Library library;
    private final VetDirectory vetDirectory;
    private final Pharmacy pharmacy;
    private Human me;
    

    public House() {
        this.garden = VegetableGarden.getInstance();
        this.pharmacy = Pharmacy.getInstance();
        this.calendar = Calendar.getInstance();
        this.family = Family.getInstance();
        this.library = Library.getInstance();
        this.vetDirectory = VetDirectory.getInstance();
        this.farm = new Farm();
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
        this.family.add(me);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public Family getFamily() {
        return this.family;
    }

    public HouseIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(HouseIdentifier identifier) {
        this.identifier = identifier;
    }

    public Library getLibrary() {
        return this.library;
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


    public final VegetableGarden getGarden() {
        return this.garden;
    }
    public final Pharmacy getPharmacy() {
        return this.pharmacy;
    }

    public VetDirectory getVetDirectory() {
        return this.vetDirectory;
    }

    public Farm getFarm() {
        return this.farm;
    }


    @Override
    public HealthBook adopt(final Animal animal, final String name, final LocalDate adoptionDate) {
        Objects.requireNonNull(animal, "Animal for adoption is required.");
        Objects.requireNonNull(name, "Animal's name for adoption is required.");
        Objects.requireNonNull(adoptionDate, "Animal's adoption date for adoption is required.");
        return this.farm.adopt(animal, name, adoptionDate);
    }
}
