package io.nirahtech.petvet.core.clinic;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class SurgeryIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private SurgeryIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final SurgeryIdentifier of(final long id) {
        if (SurgeryIdentifier.lastGeneratedIdentifier < id) {
            SurgeryIdentifier.lastGeneratedIdentifier = id;
        }
        return new SurgeryIdentifier(id);
    }

    public static final SurgeryIdentifier generate() {
        final long id = SurgeryIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}