package io.nirahtech.petvet.core.planning;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public class Event implements Serializable {
    private LocalDateTime dateTime;
    private Duration duration;

    public Event(LocalDateTime dateTime, Duration duration) {
        this.dateTime = dateTime;
        this.duration = duration;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }
    public Duration getDuration() {
        return this.duration;
    }
}
