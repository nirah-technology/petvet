package io.nirahtech.petvet.core.base;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.contact.Contact;

public class Human implements Comparator<Human>, Serializable {
    private HumanIdentifier identifier;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private final Contact contact;

    public Human(
        final String firstName,
        final String lastName
    ) {
        this.firstName = Objects.requireNonNull(firstName, "Firstname for human is required.");
        this.lastName = Objects.requireNonNull(lastName, "Lastname for human is required.");
        this.contact = new Contact();
    }

    public HumanIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(HumanIdentifier identifier) {
        this.identifier = identifier;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
        result = prime * result + ((contact == null) ? 0 : contact.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Human other = (Human) obj;
        if (identifier == null) {
            if (other.identifier != null)
                return false;
        } else if (!identifier.equals(other.identifier))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (birthDate == null) {
            if (other.birthDate != null)
                return false;
        } else if (!birthDate.equals(other.birthDate))
            return false;
        if (contact == null) {
            if (other.contact != null)
                return false;
        } else if (!contact.equals(other.contact))
            return false;
        return true;
    }

    @Override
    public int compare(Human current, Human other) {
        int lastNameComparison = current.lastName.compareTo(other.lastName);
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }

        int firstNameComparison = current.firstName.compareTo(other.firstName);
        if (firstNameComparison != 0) {
            return firstNameComparison;
        }

        return current.getBirthDate().orElse(LocalDate.MIN).compareTo(other.getBirthDate().orElse(LocalDate.MIN));

    }

    
}
