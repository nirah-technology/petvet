package io.nirahtech.petvet.installer.ui.widgets.jstepper;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class JStepPanel extends JPanel {

    private final Stepper stepper;

    private final JStepperMenuPanel jStepperMenuPanel;
    private final JStepperNavigationPanel jStepperNavigationPanel;

    public JStepPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.stepper = new Stepper();
        this.jStepperMenuPanel = new JStepperMenuPanel();
        this.jStepperNavigationPanel = new JStepperNavigationPanel(this.stepper);

        
        final JPanel stepPanel = new JPanel(new BorderLayout());
        
        this.stepper.setOnSelectedStepChanged(step -> {
            stepPanel.removeAll();
            stepPanel.add(step.getPanel(), BorderLayout.CENTER);
        });
        
        this.add(this.jStepperMenuPanel, BorderLayout.NORTH);
        this.add(stepPanel, BorderLayout.CENTER);
        this.add(this.jStepperNavigationPanel, BorderLayout.SOUTH);
    }

    public void addStep(final String title, final String description, final JPanel panel) {
        final boolean isSelected = this.stepper.getSteps().isEmpty();
        final Step step = this.stepper.addStep(title, description, panel);
        step.setSelected(isSelected);
        this.jStepperMenuPanel.addStep(step);
    }

}
