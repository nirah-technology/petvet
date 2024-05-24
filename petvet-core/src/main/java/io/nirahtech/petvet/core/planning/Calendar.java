package io.nirahtech.petvet.core.planning;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.events.Event;

public class Calendar  implements Serializable {
    private final Set<Event> events;

    public Calendar() {
        this.events = new HashSet<>();
    }
}
