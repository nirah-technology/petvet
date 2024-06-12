package io.nirahtech.petvet.core.planning;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class EventRecall implements Serializable, Comparator<EventRecall> {
    private static final Set<EventRecall> INSTANCES = new HashSet<>();

    private final int delayBeforeEvent;
    private final ChronoUnit delayChronoUnitBeforeEvent;

    private EventRecall(final int delayBeforeEvent, final ChronoUnit delayChronoUnitBeforeEvent) {
        this.delayBeforeEvent = delayBeforeEvent;
        this.delayChronoUnitBeforeEvent = delayChronoUnitBeforeEvent;
    }

    public int getDelayBeforeEvent() {
        return delayBeforeEvent;
    }

    public ChronoUnit getDelayChronoUnitBeforeEvent() {
        return delayChronoUnitBeforeEvent;
    }

    public static final EventRecall getOrCreate(final int delayBeforeEvent, final ChronoUnit delayChronoUnitBeforeEvent) {
    
        Optional<EventRecall> eventRecallFound = INSTANCES.stream().filter(instance -> {
            return (instance.getDelayBeforeEvent() == delayBeforeEvent) && (instance.getDelayChronoUnitBeforeEvent().equals(delayChronoUnitBeforeEvent));
        })
        .findFirst();
        EventRecall eventRecall;
        if (eventRecallFound.isPresent()) {
            eventRecall = eventRecallFound.get();
        } else {
            eventRecall = new EventRecall(delayBeforeEvent, delayChronoUnitBeforeEvent);
            INSTANCES.add(eventRecall);
        }
        return eventRecall;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + delayBeforeEvent;
        result = prime * result + ((delayChronoUnitBeforeEvent == null) ? 0 : delayChronoUnitBeforeEvent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventRecall other = (EventRecall) obj;
        if (delayBeforeEvent != other.delayBeforeEvent)
            return false;
        if (delayChronoUnitBeforeEvent != other.delayChronoUnitBeforeEvent)
            return false;
        return true;
    }

    @Override
    public int compare(EventRecall current, EventRecall other) {
        if (current == null && other == null) {
            return 0;
        } else if (current == null) {
            return -1;
        } else if (other == null) {
            return 1;
        }

        // First, compare by delayChronoUnitBeforeEvent
        int chronoUnitComparison = current.delayChronoUnitBeforeEvent.compareTo(other.delayChronoUnitBeforeEvent);
        if (chronoUnitComparison != 0) {
            return chronoUnitComparison;
        }

        // If delayChronoUnitBeforeEvent is the same, compare by delayBeforeEvent
        return Integer.compare(current.delayBeforeEvent, other.delayBeforeEvent);
    }
}
