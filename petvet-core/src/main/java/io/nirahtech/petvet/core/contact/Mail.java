package io.nirahtech.petvet.core.contact;

import java.util.Objects;

public final class Mail {
    private final int streetNumber;
    private final boolean isBis;
    private final Street street;
    private final City city;
    private final Province province;
    private final Country country;

    /**
     * @param streetNumber
     * @param isBis
     * @param street
     * @param city
     * @param province
     * @param country
     */
    private Mail(
            final int streetNumber,
            final boolean isBis,
            final Street street,
            final City city,
            final Province province,
            final Country country) {
        this.streetNumber = streetNumber;
        this.isBis = isBis;
        this.street = Objects.requireNonNull(street, "Street for mail is required.");
        this.city = Objects.requireNonNull(city, "City for mail is required.");
        this.province = Objects.requireNonNull(province, "Province for mail is required.");
        this.country = Objects.requireNonNull(country, "Country for mail is required.");
    }

    /**
     * @return the streetNumber
     */
    public final int getStreetNumber() {
        return this.streetNumber;
    }

    /**
     * @return the isBis
     */
    public final boolean isBis() {
        return this.isBis;
    }

    /**
     * @return the street
     */
    public final Street getStreet() {
        return this.street;
    }

    /**
     * @return the city
     */
    public final City getCity() {
        return this.city;
    }

    /**
     * @return the province
     */
    public final Province getProvince() {
        return this.province;
    }

    /**
     * @return the country
     */
    public final Country getCountry() {
        return this.country;
    }

    public static final Mail of(
            final int streetNumber,
            final boolean isBis,
            final Street street,
            final City city,
            final Province province,
            final Country country) {
        if (Objects.nonNull(city) && Objects.nonNull(province)) {
            final String cityZipCode = String.valueOf(city.getZipCode());
            final String provinceDeptNumber = String.valueOf(province.getDepartmentNumber());
            if (!cityZipCode.startsWith(provinceDeptNumber)) {
                throw new IllegalArgumentException("Zip code must start with the province departement number.");
            }
        }
        return new Mail(streetNumber, isBis, street, city, province, country);
    }

}
