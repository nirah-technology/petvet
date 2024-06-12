package io.nirahtech.petvet.core.base;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public final class Family implements Group<Human> {

    private static Family instance;

    public static Family getInstance() {
        if (Objects.isNull(instance)) {
            instance = new Family();
        }
        return instance;
    }

    private final Set<Human> members;

    private Family() {
        this.members = new HashSet<>();
    }

    @Override
    public void add(Human elementToAdd) {
        Objects.requireNonNull(elementToAdd, "Human to add is required for Familly");
        this.members.add(elementToAdd);
    }

    @Override
    public void remove(Human elementToRemove) {
        Objects.requireNonNull(elementToRemove, "Human to remove is required for Familly");
        this.members.remove(elementToRemove);
    }

    @Override
    public Stream<Human> listAll() {
        return this.members.stream();
    }

    @Override
    public boolean contains(Human elementToCheck) {
        Objects.requireNonNull(elementToCheck, "Human to check is required for Familly");
        return this.members.contains(elementToCheck);
        
    }
}
