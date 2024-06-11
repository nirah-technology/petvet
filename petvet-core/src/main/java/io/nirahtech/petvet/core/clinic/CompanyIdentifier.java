package io.nirahtech.petvet.core.clinic;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class CompanyIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0L;
    private final long id;

    private CompanyIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final CompanyIdentifier of(final long id) {
        if (CompanyIdentifier.lastGeneratedIdentifier < id) {
            CompanyIdentifier.lastGeneratedIdentifier = id;
        }
        return new CompanyIdentifier(id);
    }

    public static final CompanyIdentifier generate() {
        final long id = CompanyIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}