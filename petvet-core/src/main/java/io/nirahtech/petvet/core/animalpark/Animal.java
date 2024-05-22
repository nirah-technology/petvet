package io.nirahtech.petvet.core.animalpark;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

import io.nirahtech.petvet.core.util.Weight;

public final class Animal implements LifeCycle {
    private Species species;
    private Breed breed;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private Weight weight;
    private File picture;

    public Animal() {

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
}
