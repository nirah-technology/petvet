package io.nirahtech.petvet.core.base;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.contact.Contact;

public class Human {
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;
    private final Contact contact;

    public Human(
        final String firstName,
        final String lastName,
        final LocalDate birthDate,
        final Contact contact
    ) {
        this.firstName = Objects.requireNonNull(firstName, "Firstname for human is required.");
        this.lastName = Objects.requireNonNull(lastName, "Lastname for human is required.");
        this.birthDate = birthDate;
        this.contact = Objects.requireNonNull(contact, "Contact fo human is required.");
    }

    /**
     * @return the firstName
     */
    public final String getFirstName() {
        return this.firstName;
    }

    /**
     * @return the lastName
     */
    public final String getLastName() {
        return this.lastName;
    }

    /**
     * @return the birthDate
     */
    public final Optional<LocalDate> getBirthDate() {
        return Optional.ofNullable(this.birthDate);
    }

    /**
     * @return the contact
     */
    public final Contact getContact() {
        return this.contact;
    }

}
