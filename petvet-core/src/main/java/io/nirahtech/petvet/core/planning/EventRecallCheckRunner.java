package io.nirahtech.petvet.core.planning;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.SortedSet;
import java.util.function.Consumer;

final class EventRecallCheckRunner implements Runnable {

    private final Collection<Event> events;
    private final SortedSet<EventRecall> eventRecalls;
    private final Consumer<Event> notificationTask;

    EventRecallCheckRunner(final Collection<Event> events, final SortedSet<EventRecall> eventRecalls,
            final Consumer<Event> notificationTask) {
        this.events = events;
        this.eventRecalls = eventRecalls;
        this.notificationTask = notificationTask;
    }

    @Override
    public void run() {
        final LocalDateTime now = LocalDateTime.now();
        this.events.stream()
                .filter(event -> event.getDateTime().isAfter(LocalDateTime.now()))
                .forEach(event -> {
                    final LocalDateTime eventDateTime = event.getDateTime();
                    this.eventRecalls.forEach(recall -> {
                        final LocalDateTime recallDateTime = eventDateTime.minus(recall.getDelayBeforeEvent(),
                                recall.getDelayChronoUnitBeforeEvent());
                        if (now.isAfter(recallDateTime.minus(1, ChronoUnit.MINUTES))) {
                            if (now.isBefore(recallDateTime.plus(1, ChronoUnit.MINUTES))) {
                                this.notificationTask.accept(event);
                            }
                        }
                    });
                });
    }
}
