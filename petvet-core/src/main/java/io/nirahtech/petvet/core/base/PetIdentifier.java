package io.nirahtech.petvet.core.base;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class PetIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private PetIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final PetIdentifier of(final long id) {
        if (PetIdentifier.lastGeneratedIdentifier < id) {
            PetIdentifier.lastGeneratedIdentifier = id;
        }
        return new PetIdentifier(id);
    }

    public static final PetIdentifier generate() {
        final long id = PetIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}