package io.nirahtech.petvet.core.planning;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Event implements Serializable {
    private EventIdentifier identifier;

    private String name;
    private String description;
    
    private LocalDateTime dateTime;
    private Duration duration;
    private boolean isRepeating;
    private long repeatInterval;
    private ChronoUnit repeatUnit;
    private int totalRepeatCycles;
    private EventType eventType;


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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }


    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public boolean isRepeating() {
        return isRepeating;
    }

    public void setRepeating(boolean isRepeating) {
        this.isRepeating = isRepeating;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public int getTotalRepeatCycles() {
        return totalRepeatCycles;
    }

    public void setTotalRepeatCycles(int totalRepeatCycles) {
        this.totalRepeatCycles = totalRepeatCycles;
    }

    public ChronoUnit getRepeatUnit() {
        return repeatUnit;
    }

    public void setRepeatUnit(ChronoUnit repeatUnit) {
        this.repeatUnit = repeatUnit;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
