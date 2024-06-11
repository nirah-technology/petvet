package io.nirahtech.petvet.core.clinic;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class HealthBookIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private HealthBookIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final HealthBookIdentifier of(final long id) {
        if (HealthBookIdentifier.lastGeneratedIdentifier < id) {
            HealthBookIdentifier.lastGeneratedIdentifier = id;
        }
        return new HealthBookIdentifier(id);
    }

    public static final HealthBookIdentifier generate() {
        final long id = HealthBookIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}