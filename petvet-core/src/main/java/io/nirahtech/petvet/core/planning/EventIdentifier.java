package io.nirahtech.petvet.core.planning;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class EventIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private EventIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final EventIdentifier of(final long id) {
        if (EventIdentifier.lastGeneratedIdentifier < id) {
            EventIdentifier.lastGeneratedIdentifier = id;
        }
        return new EventIdentifier(id);
    }

    public static final EventIdentifier generate() {
        final long id = EventIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}