package io.nirahtech.petvet.installer.ui.widgets.jstepper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JPanel;

final class Stepper {
    private final List<Step> steps = new ArrayList<>();

    private Runnable onPreviousStepSelected = null;
    private Runnable onNextStepSelected = null;
    private Consumer<Step> onSelectedStepChanged = null;

    /**
     * @return the steps
     */
    public final List<Step> getSteps() {
        return Collections.unmodifiableList(this.steps);
    }

    public Step addStep(final String title, final String description, final JPanel panel) {
        final Step step = new Step(this.steps.size()+1, title, description, panel);
        if (Objects.nonNull(this.onSelectedStepChanged)) {
            step.setOnSelected(() -> onSelectedStepChanged.accept(step));
        }
        this.steps.add(step);
        return step;
    }

    public Step getSelectedStep() {
        return this.steps.stream().filter(step -> step.isSelected()).findFirst().get();
    }

    public void selectNextStep() {
        final Step current = this.steps.stream().filter(step -> step.isSelected()).findFirst().get();
        this.steps.stream().filter(step -> step.getOrder() == current.getOrder()+1).findFirst().ifPresent((next -> {
            current.unselect();
            next.select();
            if (Objects.nonNull(this.onNextStepSelected)) {
                this.onNextStepSelected.run();
            }
        }));
    }

    public void selectPreviousStep() {
        final Step current = this.steps.stream().filter(step -> step.isSelected()).findFirst().get();
        this.steps.stream().filter(step -> step.getOrder() == current.getOrder()-1).findFirst().ifPresent((previous -> {
            current.unselect();
            previous.select();
            if (Objects.nonNull(this.onPreviousStepSelected)) {
                this.onPreviousStepSelected.run();
            }
        }));
    }


    public boolean isFirstSelected() {
        return this.getSelectedStep().equals(this.steps.get(0));
    }

    public boolean isLastSelected() {
        return this.getSelectedStep().equals(this.steps.get(this.steps.size()-1));
    }
 
    /**
     * @param onNextStepSelected the onNextStepSelected to set
     */
    public void setOnNextStepSelected(Runnable onNextStepSelected) {
        this.onNextStepSelected = onNextStepSelected;
    }

    /**
     * @param onPreviousStepSelected the onPreviousStepSelected to set
     */
    public void setOnPreviousStepSelected(Runnable onPreviousStepSelected) {
        this.onPreviousStepSelected = onPreviousStepSelected;
    }

    public void setOnSelectedStepChanged(Consumer<Step> onSelectedStepChanged) {
        this.onSelectedStepChanged = onSelectedStepChanged;
    }
}
