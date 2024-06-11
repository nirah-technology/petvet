package io.nirahtech.petvet.core.pharmacy;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class ElixirIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private ElixirIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final ElixirIdentifier of(final long id) {
        if (ElixirIdentifier.lastGeneratedIdentifier < id) {
            ElixirIdentifier.lastGeneratedIdentifier = id;
        }
        return new ElixirIdentifier(id);
    }

    public static final ElixirIdentifier generate() {
        final long id = ElixirIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}