package io.nirahtech.petvet.core.planning;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class Calendar implements Serializable {
    private CalendarIdentifier identifier;
    private static Calendar instance;
    private static final long DELAY_IN_MILLIS_FOR_PLANNIFIED_TASK_EXECUTOR_TO_RUN_CYCLE = 1;
    private static final TimeUnit DELAY_UNIT_FOR_PLANNIFIED_TASK_EXECUTOR_TO_RUN_CYCLE = TimeUnit.MINUTES;
    private static final int DELAY_IN_MILLIS_FOR_PLANNIFIED_TASK_EXECUTOR_TO_WAIT = 10;
    private static final TimeUnit DELAY_UNIT_FOR_PLANNIFIED_TASK_EXECUTOR_TO_WAIT = TimeUnit.SECONDS;

    public static final Calendar getInstance() {
        if (Objects.isNull(instance)) {
            instance = new Calendar();
        }
        return instance;
    }
    
    private final ScheduledExecutorService planniedTasksRunner;
    private final Set<Event> events;
    private final SortedSet<EventRecall> eventRecalls;

    private Consumer<Event> notificationToThrow;

    private Calendar() {
        this.events = new HashSet<>();
        this.eventRecalls = new TreeSet<>();
        this.planniedTasksRunner = Executors.newSingleThreadScheduledExecutor();
    }

    public final void addEventRecall(final EventRecall eventRecall) {
        this.eventRecalls.add(eventRecall);
    }

    public void setNotificationToThrow(final Consumer<Event> notificationToThrow) {
        this.notificationToThrow = notificationToThrow;
        if (!this.planniedTasksRunner.isTerminated()) {
            try {
                this.planniedTasksRunner.awaitTermination(
                    DELAY_IN_MILLIS_FOR_PLANNIFIED_TASK_EXECUTOR_TO_WAIT, 
                    DELAY_UNIT_FOR_PLANNIFIED_TASK_EXECUTOR_TO_WAIT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Objects.nonNull(this.notificationToThrow)) {
            final Runnable eventRecallCheckRunner = new EventRecallCheckRunner(this.events, this.eventRecalls, this.notificationToThrow);
            this.planniedTasksRunner.scheduleAtFixedRate(
                eventRecallCheckRunner,
                0,
                DELAY_IN_MILLIS_FOR_PLANNIFIED_TASK_EXECUTOR_TO_RUN_CYCLE, 
                DELAY_UNIT_FOR_PLANNIFIED_TASK_EXECUTOR_TO_RUN_CYCLE);
        }
    }

    public CalendarIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CalendarIdentifier identifier) {
        this.identifier = identifier;
    }

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
}
