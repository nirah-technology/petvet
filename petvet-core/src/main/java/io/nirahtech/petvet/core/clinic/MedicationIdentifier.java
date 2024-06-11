package io.nirahtech.petvet.core.clinic;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class MedicationIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0L;
    private final long id;

    private MedicationIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final MedicationIdentifier of(final long id) {
        if (MedicationIdentifier.lastGeneratedIdentifier < id) {
            MedicationIdentifier.lastGeneratedIdentifier = id;
        }
        return new MedicationIdentifier(id);
    }

    public static final MedicationIdentifier generate() {
        final long id = MedicationIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}