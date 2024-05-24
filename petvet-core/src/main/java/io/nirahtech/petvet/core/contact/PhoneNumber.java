package io.nirahtech.petvet.core.contact;

import java.io.Serializable;

public final class PhoneNumber implements Serializable {
    private final int countryCode;
    private final int phoneNumber;

    private PhoneNumber(
        final int countryCode,
        final int phoneNumber
    ) {
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the countryCode
     */
    public final int getCountryCode() {
        return this.countryCode;
    }
    /**
     * @return the phoneNumber
     */
    public final int getPhoneNumber() {
        return this.phoneNumber;
    }

    public static final PhoneNumber of(final int countryCode, final int phoneNumber) {
        return new PhoneNumber(countryCode, phoneNumber);
    }
}
