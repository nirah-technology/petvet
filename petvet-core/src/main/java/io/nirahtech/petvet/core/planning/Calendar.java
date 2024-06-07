package io.nirahtech.petvet.core.planning;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Calendar  implements Serializable {
    private final Set<Event> events;

    public Calendar() {
        this.events = new HashSet<>();
    }
}
