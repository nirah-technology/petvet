package io.nirahtech.petvet.core.base;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.animalpark.Animal;

public final class Pet implements Serializable {
    private PetIdentifier identifier;
    private final Animal animal;
    private final String name;
    private final LocalDate adoptionDate;
    private boolean isTatooed;
    private Microship microship;

    public Pet(
        final Animal animal,
        final String name,
        final LocalDate adoptionDate
    ) {
        this.animal = Objects.requireNonNull(animal, "Animal for pet is required");
        this.name = Objects.requireNonNull(name, "Name for pet is required.");
        this.adoptionDate = Objects.requireNonNull(adoptionDate, "Adoption date for pet is required.");
        this.isTatooed = false;
    }

    public PetIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(PetIdentifier identifier) {
        this.identifier = identifier;
    }
    
    public Optional<Microship> getMicroship() {
        return Optional.ofNullable(this.microship);
    }
    public void setMicroship(Microship microship) {
        this.microship = microship;
    }
    public void setTatooed(boolean isTatooed) {
        this.isTatooed = isTatooed;
    }
    public boolean isTatooed() {
        return this.isTatooed;
    }
    /**
     * @return the animal
     */
    public final Animal getAnimal() {
        return this.animal;
    }
    /**
     * @return the adoptionDate
     */
    public final LocalDate getAdoptionDate() {
        return this.adoptionDate;
    }
    /**
     * @return the name
     */
    public final String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((animal == null) ? 0 : animal.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((adoptionDate == null) ? 0 : adoptionDate.hashCode());
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
        Pet other = (Pet) obj;
        if (animal == null) {
            if (other.animal != null)
                return false;
        } else if (!animal.equals(other.animal))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (adoptionDate == null) {
            if (other.adoptionDate != null)
                return false;
        } else if (!adoptionDate.equals(other.adoptionDate))
            return false;
        return true;
    }

    
}
