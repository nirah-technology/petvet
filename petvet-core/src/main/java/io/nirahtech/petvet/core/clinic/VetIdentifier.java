package io.nirahtech.petvet.core.clinic;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class VetIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0L;
    private final long id;

    private VetIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final VetIdentifier of(final long id) {
        if (VetIdentifier.lastGeneratedIdentifier < id) {
            VetIdentifier.lastGeneratedIdentifier = id;
        }
        return new VetIdentifier(id);
    }

    public static final VetIdentifier generate() {
        final long id = VetIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}