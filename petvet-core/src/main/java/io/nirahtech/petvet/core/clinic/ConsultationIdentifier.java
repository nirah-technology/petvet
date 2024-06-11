package io.nirahtech.petvet.core.clinic;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class ConsultationIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private ConsultationIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final ConsultationIdentifier of(final long id) {
        if (ConsultationIdentifier.lastGeneratedIdentifier < id) {
            ConsultationIdentifier.lastGeneratedIdentifier = id;
        }
        return new ConsultationIdentifier(id);
    }

    public static final ConsultationIdentifier generate() {
        final long id = ConsultationIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}