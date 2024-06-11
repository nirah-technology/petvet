package io.nirahtech.petvet.features.planner.implementations;

import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.Event;
import io.nirahtech.petvet.features.planner.ModifyEventFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultModifyEventFeatureImpl implements ModifyEventFeature {

    private static ModifyEventFeature instance;

    public static final ModifyEventFeature getInstance() {
        if (Objects.isNull(instance)) {
            final Calendar calendar = Calendar.getInstance();
            instance = new DefaultModifyEventFeatureImpl(calendar);
        }
        return instance;
    }

    private final Calendar calendar;

    private DefaultModifyEventFeatureImpl(final Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public void modifyEvent(Event eventToModify) throws FeatureExecutionException {
        Objects.requireNonNull(eventToModify, "Event is required for ModifyEventFeature");
        final Optional<Event> eventFound = this.calendar.getEvents().filter(event -> event.equals(eventToModify)).findFirst(); 
        if (eventFound.isPresent()) {
            this.calendar.delete(eventToModify);
            this.calendar.add(eventToModify);
        }
    }
    
}
