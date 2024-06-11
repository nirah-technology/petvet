package io.nirahtech.petvet.core.clinic;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class PrescriptionIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private PrescriptionIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final PrescriptionIdentifier of(final long id) {
        if (PrescriptionIdentifier.lastGeneratedIdentifier < id) {
            PrescriptionIdentifier.lastGeneratedIdentifier = id;
        }
        return new PrescriptionIdentifier(id);
    }

    public static final PrescriptionIdentifier generate() {
        final long id = PrescriptionIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}