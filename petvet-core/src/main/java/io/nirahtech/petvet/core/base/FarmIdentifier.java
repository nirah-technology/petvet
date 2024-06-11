package io.nirahtech.petvet.core.base;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class FarmIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0L;
    private final long id;

    private FarmIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final FarmIdentifier of(final long id) {
        if (FarmIdentifier.lastGeneratedIdentifier < id) {
            FarmIdentifier.lastGeneratedIdentifier = id;
        }
        return new FarmIdentifier(id);
    }

    public static final FarmIdentifier generate() {
        final long id = FarmIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}