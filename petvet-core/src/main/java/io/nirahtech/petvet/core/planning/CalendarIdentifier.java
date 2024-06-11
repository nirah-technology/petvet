package io.nirahtech.petvet.core.planning;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class CalendarIdentifier implements Identifier {

    private static long lastGeneratedIdentifier = 0;
    private final long id;

    private CalendarIdentifier(final long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }


    public static final CalendarIdentifier of(final long id) {
        if (CalendarIdentifier.lastGeneratedIdentifier < id) {
            CalendarIdentifier.lastGeneratedIdentifier = id;
        }
        return new CalendarIdentifier(id);
    }

    public static final CalendarIdentifier generate() {
        final long id = CalendarIdentifier.lastGeneratedIdentifier++;
        return of(id);
    }

}