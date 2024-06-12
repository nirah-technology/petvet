package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.nirahtech.petvet.core.contact.Contact;

public final class Company implements Serializable {
    private static final Map<String, Company> INSTANCES = new HashMap<>();


    private CompanyIdentifier identifier;
    private final String name;
    private final Contact contact;

    private Company(
        final String name
    ) {
        this.name = Objects.requireNonNull(name, "Name for company is required.");
        this.contact = new Contact();
    }
    
    public CompanyIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(CompanyIdentifier identifier) {
        this.identifier = identifier;
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


    private static final String sanitize(final String name) {
        return name.toLowerCase();
    }

    public static final Company of(final String name) {
        final String key = sanitize(name);
        return INSTANCES.computeIfAbsent(key, Company::new);
    }
}
