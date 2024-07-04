package io.nirahtech.petvet.installer.ui.widgets.jstepper;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class JStepperNavigationPanel extends JPanel {
    
    private final Stepper stepper;
    private final JButton previousButton;
    private final JButton nextButton;


    public JStepperNavigationPanel(final Stepper stepper) {
        super(new BorderLayout());
        this.stepper = stepper;

        this.previousButton = new JButton("Previous");
        this.nextButton = new JButton("Next");

        this.previousButton.addActionListener(event -> {
            final Step current = this.stepper.getSelectedStep();
            if (current.getOrder() > 0) {
                this.stepper.selectPreviousStep();
            }
        });


        this.nextButton.addActionListener(event -> {
            final Step current = this.stepper.getSelectedStep();
            final Step last = this.stepper.getSteps().stream().reduce((first, second) -> second).get();
            if (current.getOrder() < last.getOrder()) {
                this.stepper.selectNextStep();
            }
        });

        this.add(this.previousButton, BorderLayout.WEST);
        this.add(this.nextButton, BorderLayout.EAST);

    }

    final void redraw() {
        if (!this.stepper.isFirstSelected()) {
            this.previousButton.setVisible(true);
            this.previousButton.setEnabled(true);
        } else {
            this.previousButton.setVisible(false);
            this.previousButton.setEnabled(false);
        }

        if (!this.stepper.isLastSelected()) {
            this.nextButton.setVisible(true);
            this.nextButton.setEnabled(true);
        } else {
            this.nextButton.setVisible(false);
            this.nextButton.setEnabled(false);
        }
    }

}
