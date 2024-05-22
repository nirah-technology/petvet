package io.nirahtech.petvet.core.planning;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.events.Event;

public class Calendar {
    private final Set<Event> events;

    public Calendar() {
        this.events = new HashSet<>();
    }
}
