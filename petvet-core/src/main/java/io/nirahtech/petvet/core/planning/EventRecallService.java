package io.nirahtech.petvet.core.planning;

import java.util.Collection;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class EventRecallService  {
    
    private static final long DELAY_IN_MILLIS_FOR_PLANNIFIED_TASK_EXECUTOR_TO_RUN_CYCLE = 1;
    private static final TimeUnit DELAY_UNIT_FOR_PLANNIFIED_TASK_EXECUTOR_TO_RUN_CYCLE = TimeUnit.MINUTES;
    private static final int DELAY_IN_MILLIS_FOR_PLANNIFIED_TASK_EXECUTOR_TO_WAIT = 10;
    private static final TimeUnit DELAY_UNIT_FOR_PLANNIFIED_TASK_EXECUTOR_TO_WAIT = TimeUnit.SECONDS;


    private final ScheduledExecutorService planniedTasksRunner;
    private final SortedSet<EventRecall> eventRecalls;
    private Consumer<Event> notificationToThrow;
    private final Collection<Event> events;

    public EventRecallService(final Collection<Event> events) {
        this.eventRecalls = new TreeSet<>();
        this.planniedTasksRunner = Executors.newSingleThreadScheduledExecutor();
        this.events = events;
    }


    public final void addEventRecall(final EventRecall eventRecall) {
        this.eventRecalls.add(eventRecall);
    }

    public void setNotificationToThrow(final Consumer<Event> notificationToThrow) {
        this.notificationToThrow = notificationToThrow;
    }
    
    public void start() {
        
        if (Objects.nonNull(this.notificationToThrow)) {
            final Runnable eventRecallCheckRunner = new EventRecallCheckRunner(this.events, this.eventRecalls, this.notificationToThrow);
            this.planniedTasksRunner.scheduleAtFixedRate(
                eventRecallCheckRunner,
                0,
                DELAY_IN_MILLIS_FOR_PLANNIFIED_TASK_EXECUTOR_TO_RUN_CYCLE, 
                DELAY_UNIT_FOR_PLANNIFIED_TASK_EXECUTOR_TO_RUN_CYCLE);
        }

    }

    public void stop() {
        if (!this.planniedTasksRunner.isTerminated()) {
            try {
                this.planniedTasksRunner.awaitTermination(
                    DELAY_IN_MILLIS_FOR_PLANNIFIED_TASK_EXECUTOR_TO_WAIT, 
                    DELAY_UNIT_FOR_PLANNIFIED_TASK_EXECUTOR_TO_WAIT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
