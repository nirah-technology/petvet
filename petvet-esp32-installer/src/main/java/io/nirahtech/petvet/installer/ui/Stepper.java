package io.nirahtech.petvet.installer.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Stepper {
    private final List<Step> steps = new ArrayList<>();

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
            }
        }
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
}
