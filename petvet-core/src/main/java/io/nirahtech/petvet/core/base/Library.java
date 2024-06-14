package io.nirahtech.petvet.core.base;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class Library implements Group<PhotoBook> {

    private static Library instance;
    public static Library getInstance() {
        if (Objects.isNull(instance)) {
            instance = new Library();
        }
        return instance;
    }

    private final Set<PhotoBook> photoBooks;

    private Library() {
        this.photoBooks = new HashSet<>();
    }


    @Override
    public void add(PhotoBook elementToAdd) {
        Objects.requireNonNull(elementToAdd, "PhotoBook to add is required for Library");
        this.photoBooks.add(elementToAdd);
    }

    @Override
    public void remove(PhotoBook elementToRemove) {
        Objects.requireNonNull(elementToRemove, "PhotoBook to remove is required for Library");
        this.photoBooks.remove(elementToRemove);
    }

    @Override
    public Stream<PhotoBook> listAll() {
        return this.photoBooks.stream();
    }

    @Override
    public boolean contains(PhotoBook elementToCheck) {
        Objects.requireNonNull(elementToCheck, "Human to check is required for Familly");
        return this.photoBooks.contains(elementToCheck);
    }

    public Optional<PhotoBook> getFor(final Identifier petIdentifier) {
        return this.listAll()
                .filter(book -> book.getPet().getIdentifier().equals(petIdentifier))
                .findFirst();
    }
    
}
