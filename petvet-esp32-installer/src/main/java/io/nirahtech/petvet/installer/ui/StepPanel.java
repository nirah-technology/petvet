package io.nirahtech.petvet.installer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public final class StepPanel extends JPanel {

    private final Color SELECT_STEP_LABEL_COLOR = new Color(255,255,255);
    private final Color UNSELECT_STEP_LABEL_COLOR = new Color(175,175,175, 100);

    private final Step step;
    private final JButton button;
    private final JLabel label;

    private Consumer<Step> onSelectedStepEventHandler = null;

    public StepPanel(final Step step) {
        super(new BorderLayout());
        this.step = step;

        final JPanel panel = new JPanel(new GridLayout(2, 1));
        this.button = new JButton(String.valueOf(this.step.getValue()));
        this.label = new JLabel(this.step.getDescription());
        this.label.setHorizontalAlignment(SwingConstants.CENTER);
        this.button.addActionListener(event -> {
            if (Objects.nonNull(this.onSelectedStepEventHandler)) {
                onSelectedStepEventHandler.accept(step);
            }
            this.step.getGraphicComponent().setVisible(this.step.isSelected());            
        });
        button.setEnabled(this.step.isSelected());
        
        panel.add(this.button);
        panel.add(this.label);
        this.add(panel, BorderLayout.CENTER);
    }

    /**
     * @return the step
     */
    public final Step getStep() {
        return this.step;
    }

    public final void setOnSelectedStep(final Consumer<Step> onSelectedStepEventHandler) {
        this.onSelectedStepEventHandler = onSelectedStepEventHandler;
    }

    public final void redraw() {
        this.button.setEnabled(this.step.isSelected());
        if (this.step.isSelected()) {
            this.label.setForeground(SELECT_STEP_LABEL_COLOR);
        } else {
            this.label.setForeground(UNSELECT_STEP_LABEL_COLOR);

        }
    }

}
