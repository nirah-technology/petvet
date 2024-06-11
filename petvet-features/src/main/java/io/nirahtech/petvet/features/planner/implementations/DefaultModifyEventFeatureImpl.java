package io.nirahtech.petvet.features.planner.implementations;

import java.util.Objects;

import io.nirahtech.petvet.core.planning.Calendar;
import io.nirahtech.petvet.core.planning.EventIdentifier;
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
    public void modifyEvent(EventIdentifier identifierOfTheEventToModify) throws FeatureExecutionException {
        Objects.requireNonNull(identifierOfTheEventToModify, "Event is required for ModifyEventFeature");
        this.calendar.getEvents()
                .filter(event -> event.getIdentifier().equals(identifierOfTheEventToModify))
                .findFirst()
                .ifPresent(eventToModify -> {
                    this.calendar.delete(eventToModify);
                    this.calendar.add(eventToModify);
                });
    }
}
