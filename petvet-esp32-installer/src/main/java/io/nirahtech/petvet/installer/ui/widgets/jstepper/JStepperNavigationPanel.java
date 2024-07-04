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

        this.add(this.previousButton, BorderLayout.WEST);
        this.add(this.nextButton, BorderLayout.EAST);

    }

}
