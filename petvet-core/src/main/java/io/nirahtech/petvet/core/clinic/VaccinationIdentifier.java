package io.nirahtech.petvet.core.clinic;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class VaccinationIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private VaccinationIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final VaccinationIdentifier of(final long id) {
        if (VaccinationIdentifier.lastGeneratedIdentifier < id) {
            VaccinationIdentifier.lastGeneratedIdentifier = id;
        }
        return new VaccinationIdentifier(id);
    }

    public static final VaccinationIdentifier generate() {
        final long id = VaccinationIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}