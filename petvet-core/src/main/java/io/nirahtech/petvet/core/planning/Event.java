package io.nirahtech.petvet.core.planning;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public class Event implements Serializable {
    private EventIdentifier identifier;
    private LocalDateTime dateTime;
    private Duration duration;

    public Event(LocalDateTime dateTime, Duration duration) {
        this.dateTime = dateTime;
        this.duration = duration;
    }
    public EventIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(EventIdentifier identifier) {
        this.identifier = identifier;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }
    public Duration getDuration() {
        return this.duration;
    }
}
