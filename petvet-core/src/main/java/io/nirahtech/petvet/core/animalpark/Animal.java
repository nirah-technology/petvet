package io.nirahtech.petvet.core.animalpark;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

import io.nirahtech.petvet.core.util.Weight;

public final class Animal implements LifeCycle, Serializable {
    private AnimalIdentifier identifier;
    private Species species;
    private Breed breed;
    private Gender gender;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private Weight weight;
    private File picture;

    public Animal() {

    }

    public AnimalIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(AnimalIdentifier identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the species
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * @param species the species to set
     */
    public void setSpecies(Species species) {
        this.species = species;
    }

    /**
     * @return the breed
     */
    public Breed getBreed() {
        return breed;
    }

    /**
     * @param breed the breed to set
     */
    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    /**
     * @return the birthDate
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * @return the deathDate
     */
    public Optional<LocalDate> getDeathDate() {
        return Optional.ofNullable(this.deathDate);
    }

    
    /**
     * @return the weight
     */
    public Weight getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    /**
     * @return the picture
     */
    public File getPicture() {
        return picture;
    }

    /**
     * @param picture the picture to set
     */
    public void setPicture(File picture) {
        this.picture = picture;
    }

    @Override
    public final void birth(final LocalDate date) {
        this.birthDate = date;
    }

    @Override
    public final void death(final LocalDate date) {
        this.deathDate = date;
    }
    public final Gender getGender() {
        return this.gender;
    }
    public final void setGender(final Gender gender) {
        this.gender = gender;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        result = prime * result + ((species == null) ? 0 : species.hashCode());
        result = prime * result + ((breed == null) ? 0 : breed.hashCode());
        result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
        result = prime * result + ((deathDate == null) ? 0 : deathDate.hashCode());
        result = prime * result + ((weight == null) ? 0 : weight.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((picture == null) ? 0 : picture.hashCode());
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
        Animal other = (Animal) obj;
        if (identifier == null) {
            if (other.identifier != null)
                return false;
        } else if (!identifier.equals(other.identifier))
            return false;
        if (species == null) {
            if (other.species != null)
                return false;
        } else if (!species.equals(other.species))
            return false;
        if (breed == null) {
            if (other.breed != null)
                return false;
        } else if (!breed.equals(other.breed))
            return false;
        if (birthDate == null) {
            if (other.birthDate != null)
                return false;
        } else if (!birthDate.equals(other.birthDate))
            return false;
        if (deathDate == null) {
            if (other.deathDate != null)
                return false;
        } else if (!deathDate.equals(other.deathDate))
            return false;
        if (weight == null) {
            if (other.weight != null)
                return false;
        } else if (!weight.equals(other.weight))
            return false;
        if (picture == null) {
            if (other.picture != null)
                return false;
        } else if (!picture.equals(other.picture))
            return false;
        if (gender == null) {
            if (other.gender != null)
                return false;
        } else if (!gender.equals(other.gender))
            return false;
        return true;
    }

    
}
