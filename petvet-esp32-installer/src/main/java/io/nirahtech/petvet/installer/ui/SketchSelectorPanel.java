package io.nirahtech.petvet.installer.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SketchSelectorPanel extends ResponsiveSliderPanel {


    private final JButton nextStepButton;
    private Runnable onNext = null;
    private final JButton previousStepButton;
    private Runnable onPrevious = null;
    
    SketchSelectorPanel(Dimension dimension, final Stepper stepper) {
        super(dimension);
        this.setLayout(new GridLayout(5, 1));


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
        this.add(navigatorPanel);

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
