package io.nirahtech.petvet.core.base;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class HouseIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0L;
    private final long id;

    private HouseIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final HouseIdentifier of(final long id) {
        if (HouseIdentifier.lastGeneratedIdentifier < id) {
            HouseIdentifier.lastGeneratedIdentifier = id;
        }
        return new HouseIdentifier(id);
    }

    public static final HouseIdentifier generate() {
        final long id = HouseIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}