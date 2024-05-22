package io.nirahtech.petvet.core.clinic;

import java.util.Objects;

import io.nirahtech.petvet.core.contact.Contact;

public final class Company {
    private final String name;
    private final Contact contact;

    public Company(
        final String name,
        final Contact contact
    ) {
        this.name = Objects.requireNonNull(name, "Name for company is required.");
        this.contact = Objects.requireNonNull(contact, "Contact for company is required.");
    }
    
    /**
     * @return the name
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @return the contact
     */
    public final Contact getContact() {
        return this.contact;
    }
}
