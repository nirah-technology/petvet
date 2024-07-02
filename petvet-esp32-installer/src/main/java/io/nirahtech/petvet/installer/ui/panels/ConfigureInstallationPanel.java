package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.nirahtech.petvet.installer.ui.stepper.Stepper;

public class ConfigureInstallationPanel extends JPanel {
    
    private final JButton nextStepButton;
    private final JButton previousStepButton;
    private Runnable onNext = null;
    private Runnable onPrevious = null;

    public ConfigureInstallationPanel(final Stepper stepper) {
        super(new BorderLayout());
        this.add(new JLabel("CONFIGURE INSTALLATION"), BorderLayout.CENTER);

        this.previousStepButton = new JButton("Previous");
        this.previousStepButton.addActionListener(event -> {
            stepper.selectPreviousStep();
            if (Objects.nonNull(this.onPrevious)) {
                this.onPrevious.run();
            }
        });
        this.add(this.previousStepButton);

        this.nextStepButton = new JButton("Next");
        this.nextStepButton.addActionListener(event -> {
            stepper.selectNextStep();
            if (Objects.nonNull(this.onNext)) {
                this.onNext.run();
            }
        });


        final JPanel navigatorPanel = new JPanel(new GridLayout(1, 2));
        navigatorPanel.add(this.previousStepButton);
        navigatorPanel.add(this.nextStepButton);
        this.add(navigatorPanel, BorderLayout.SOUTH);
    }



    /**
     * @param onNext the onNext to set
     */
    public void setOnNextEventHandler(Runnable onNext) {
        this.onNext = onNext;
    }

    /**
     * @param onNext the onNext to set
     */
    public void setOnPreviousEventHandler(Runnable onPrevious) {
        this.onPrevious = onPrevious;
    }
}
