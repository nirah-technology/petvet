package io.nirahtech.petvet.core.base;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class PhotoBookIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0L;
    private final long id;

    private PhotoBookIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final PhotoBookIdentifier of(final long id) {
        if (PhotoBookIdentifier.lastGeneratedIdentifier < id) {
            PhotoBookIdentifier.lastGeneratedIdentifier = id;
        }
        return new PhotoBookIdentifier(id);
    }

    public static final PhotoBookIdentifier generate() {
        final long id = PhotoBookIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}