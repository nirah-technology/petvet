package io.nirahtech.petvet.installer.ui.stepper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class Stepper {
    private final List<Step> steps;

    private Consumer<Step> onSelectedEventHandler;
    private Consumer<Step> onUnselectedEventHandler;

    public Stepper() {
        this.steps = new ArrayList<>();
    }

    /**
     * @return the steps
     */
    public final List<Step> getSteps() {
        return Collections.unmodifiableList(this.steps);
    }

    public void addStep(final Step step) {
        if (Objects.nonNull(step)) {
            if (!this.steps.contains(step)) {
                this.steps.add(step);
                this.updateEventsListeners();
            }
        }
    }

    private final void updateEventsListeners() {
        this.steps.forEach(step -> {
            step.setOnSelectedEventHandler(this.onSelectedEventHandler);
            step.setOnUnselectedEventHandler(this.onUnselectedEventHandler);
        });
    }

    public Step getSelectedStep() {
        return this.steps.stream().filter(step -> step.isSelected()).findFirst().get();
    }

    public void selectNextStep() {
        final Step current = this.steps.stream().filter(step -> step.isSelected()).findFirst().get();
        this.steps.stream().filter(step -> step.getValue() == current.getValue()+1).findFirst().ifPresent((next -> {
            current.unselect();
            next.select();
        }));
    }

    public void selectPreviousStep() {
        final Step current = this.steps.stream().filter(step -> step.isSelected()).findFirst().get();
        this.steps.stream().filter(step -> step.getValue() == current.getValue()-1).findFirst().ifPresent((previous -> {
            current.unselect();
            previous.select();
        }));
    }

    void setOnSelectedEventHandler(Consumer<Step> onSelectedEventHandler) {
        this.onSelectedEventHandler = onSelectedEventHandler;
        this.updateEventsListeners();
    }
    void setOnUnselectedEventHandler(Consumer<Step> onUnselectedEventHandler) {
        this.onUnselectedEventHandler = onUnselectedEventHandler;
        this.updateEventsListeners();
    }
}
