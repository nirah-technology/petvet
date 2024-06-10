package io.nirahtech.petvet.core.planning;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public final class Calendar implements Serializable {

    private static Calendar instance;

    public static final Calendar getInstance() {
        if (Objects.isNull(instance)) {
            instance = new Calendar();
        }
        return instance;
    }

    private final Set<Event> events;

    public Stream<Event> getEvents() {
        return this.events.stream();
    }

    public final void add(final Event event) {
        this.events.add(event);
    }

    public final void delete(final Event event) {
        if (this.events.contains(event)) {
            this.events.remove(event);
        }
    }

    private Calendar() {
        this.events = new HashSet<>();
    }
}
