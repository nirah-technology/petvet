package io.nirahtech.petvet.core.base;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class HumanIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private HumanIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final HumanIdentifier of(final long id) {
        if (HumanIdentifier.lastGeneratedIdentifier < id) {
            HumanIdentifier.lastGeneratedIdentifier = id;
        }
        return new HumanIdentifier(id);
    }

    public static final HumanIdentifier generate() {
        final long id = HumanIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}