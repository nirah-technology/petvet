package io.nirahtech.petvet.core.clinic;

import java.util.Objects;

import io.nirahtech.petvet.core.base.Human;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.contact.Contact;

public final class Vet implements Healer {
    private VetIdentifier identifier;
    private final Human human;
    private final Company company;
    private final Contact contact;

    public Vet(
        final Human human,
        final Company company,
        final Contact contact
    ) {
        this.human = Objects.requireNonNull(human, "Human for vet is required.");
        this.company = Objects.requireNonNull(company, "Company for vet is required.");
        this.contact = Objects.requireNonNull(contact, "Contact for vet is required.");
    }

    public VetIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(VetIdentifier identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the company
     */
    public final Company getCompany() {
        return this.company;
    }
    /**
     * @return the contact
     */
    public final Contact getContact() {
        return this.contact;
    }

    /**
     * @return the human
     */
    public final Human getHuman() {
        return this.human;
    }

    @Override
    public HealthBook heal(Pet pet, Consultation consultation) {
        Objects.requireNonNull(pet, "Pet for heal is required.");
        Objects.requireNonNull(consultation, "Consultation for heal is required.");
        final HealthBook healthBook = new HealthBook(pet);
        return this.heal(pet, consultation, healthBook);
    }

    @Override
    public HealthBook heal(Pet pet, Consultation consultation, HealthBook healthBook) {
        Objects.requireNonNull(pet, "Pet for heal is required.");
        Objects.requireNonNull(consultation, "Consultation for heal is required.");
        Objects.requireNonNull(healthBook, "Healthbook for heal is required.");
        healthBook.addConsultation(consultation);
        return healthBook;
    }
}
