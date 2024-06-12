package io.nirahtech.petvet.core.base;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class PhotoBook implements Group<File> {

    private Identifier identifier; 
    private final Set<File> pictures;
    private final Pet pet;

    public PhotoBook(final Pet pet) {
        this.pet = pet;
        this.pictures = new HashSet<>();
    }

    public Identifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Pet getPet() {
        return this.pet;
    }

    @Override
    public void add(File elementToAdd) {
        Objects.requireNonNull(elementToAdd, "File to add is required for PhotoBook");
        this.pictures.add(elementToAdd);
    }

    @Override
    public void remove(File elementToRemove) {
        Objects.requireNonNull(elementToRemove, "File to remove is required for PhotoBook");
        this.pictures.remove(elementToRemove);
    }

    @Override
    public Stream<File> listAll() {
        return this.pictures.stream();
    }

    @Override
    public boolean contains(File elementToCheck) {
        Objects.requireNonNull(elementToCheck, "File to check is required for VetDirectory");
        return this.pictures.contains(elementToCheck);
    }

}
