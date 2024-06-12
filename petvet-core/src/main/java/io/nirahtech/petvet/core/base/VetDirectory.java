package io.nirahtech.petvet.core.base;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.clinic.Vet;

public final class VetDirectory implements Group<Vet> {

    private static VetDirectory instance;
    
    public static VetDirectory getInstance() {
        if (Objects.isNull(instance)) {
            instance = new VetDirectory();
        }
        return instance;
    }

    private final Set<Vet> vets;

    private VetDirectory() {
        this.vets = new HashSet<>();
    }

    @Override
    public void add(Vet elementToAdd) {
        Objects.requireNonNull(elementToAdd, "Vet to add is required for VetDirectory");
        this.vets.add(elementToAdd);
    }

    @Override
    public void remove(Vet elementToRemove) {
        Objects.requireNonNull(elementToRemove, "Vet to remove is required for VetDirectory");
        this.vets.remove(elementToRemove);
    }

    @Override
    public Stream<Vet> listAll() {
        return this.vets.stream();
    }

    @Override
    public boolean contains(Vet elementToCheck) {
        Objects.requireNonNull(elementToCheck, "Vet to check is required for VetDirectory");
        return this.vets.contains(elementToCheck);
    }

}
