package io.nirahtech.petvet.installer.ui.components.stepper;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.swing.JPanel;

public final class StepperPanel extends JPanel {

    private final Stepper stepper;

    private AtomicReference<Step> currentStep = new AtomicReference<>(null);

    private Consumer<Step> onSelectedStepEventListenerHandler = null;
    private Consumer<Step> onUnselectedStepEventListenerHandler = null;
    
    private final List<JStepPanel> stepsPanels = new ArrayList<>();
    
    public StepperPanel(final Stepper stepper) {
        super(new GridLayout(1, 5));
        this.stepper = stepper;
        this.currentStep.set(this.stepper.getSelectedStep());
        
        this.stepper.getSteps().forEach(step -> {
            final JStepPanel stepPanel = new JStepPanel(step);
            this.stepsPanels.add(stepPanel);
            this.add(stepPanel);
            this.updateEventListeners();
        });
    }

    private void updateEventListeners() {
        this.stepper.setOnSelectedEventHandler(this.onSelectedStepEventListenerHandler);
        this.stepper.setOnUnselectedEventHandler(this.onUnselectedStepEventListenerHandler);
    }

    public final int getTotalSteps() {
        return this.stepper.getSteps().size();
    }

    public void onSelectedStepEventListenerHandler(Consumer<Step> callback) {
        this.onSelectedStepEventListenerHandler = callback;
        this.updateEventListeners();
    }
    public void onUnselectedStepEventListenerHandler(Consumer<Step> callback) {
        this.onUnselectedStepEventListenerHandler = callback;
        this.updateEventListeners();
    }

    public void redraw() {
        this.stepsPanels.forEach(stepPanel -> stepPanel.redraw());
    }
}
