package io.nirahtech.petvet.core.animalpark;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class AnimalIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0L;
    private final long id;

    private AnimalIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final AnimalIdentifier of(final long id) {
        if (AnimalIdentifier.lastGeneratedIdentifier < id) {
            AnimalIdentifier.lastGeneratedIdentifier = id;
        }
        return new AnimalIdentifier(id);
    }

    public static final AnimalIdentifier generate() {
        final long id = AnimalIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}
