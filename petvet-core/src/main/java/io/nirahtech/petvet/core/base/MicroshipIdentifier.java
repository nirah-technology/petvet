package io.nirahtech.petvet.core.base;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class MicroshipIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private MicroshipIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final MicroshipIdentifier of(final long id) {
        if (MicroshipIdentifier.lastGeneratedIdentifier < id) {
            MicroshipIdentifier.lastGeneratedIdentifier = id;
        }
        return new MicroshipIdentifier(id);
    }

    public static final MicroshipIdentifier generate() {
        final long id = MicroshipIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}