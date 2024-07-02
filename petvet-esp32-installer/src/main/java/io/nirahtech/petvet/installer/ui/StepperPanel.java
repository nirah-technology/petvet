package io.nirahtech.petvet.installer.ui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.swing.JPanel;

public final class StepperPanel extends JPanel {

    private final Stepper stepper;

    private AtomicReference<Step> currentStep = new AtomicReference<>(null);

    private Consumer<Step> onStepChangeChangeEventHandler = null;
    private final List<StepPanel> stepsPanels = new ArrayList<>();
    
    StepperPanel(final Stepper stepper) {
        super(new GridLayout(1, 5));
        this.stepper = stepper;
        this.currentStep.set(this.stepper.getSelectedStep());
        
        this.stepper.getSteps().forEach(step -> {
            final StepPanel stepPanel = new StepPanel(step);
            stepPanel.setOnSelectedStep((selectedStep) -> {
                if (Objects.nonNull(this.onStepChangeChangeEventHandler)) {
                    this.onStepChangeChangeEventHandler.accept(selectedStep);
                }
            });
            this.stepsPanels.add(stepPanel);
            this.add(stepPanel);
        });
        
    }

    public final int getTotalSteps() {
        return this.stepper.getSteps().size();
    }

    public void onStepChanged(Consumer<Step> callback) {
        this.onStepChangeChangeEventHandler = callback;
    }

    public void redraw() {
        this.stepsPanels.forEach(stepPanel -> stepPanel.redraw());
    }
}
