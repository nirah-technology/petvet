package io.nirahtech.petvet.installer.ui.widgets.jstepper;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

final class JStepMenuItemPanel extends JPanel {

    private static final Color SELECT_STEP_LABEL_COLOR = new Color(255,255,255);
    private static final Color UNSELECT_STEP_LABEL_COLOR = new Color(175,175,175, 100);

    private final Step step;

    private final JLabel order;
    private final JLabel title;

    JStepMenuItemPanel(final Step step) {
        super(new BorderLayout());
        this.step = step;
        this.setToolTipText(this.step.getDescription());
        
        this.order = new JLabel(String.valueOf(this.step.getOrder()));
        this.title = new JLabel(this.step.getTitle());

        this.order.setHorizontalAlignment(SwingConstants.CENTER);
        this.order.setVerticalAlignment(SwingConstants.CENTER);

        this.title.setHorizontalAlignment(SwingConstants.CENTER);
        this.title.setVerticalAlignment(SwingConstants.CENTER);

        
        this.add(this.order, BorderLayout.CENTER);
        this.add(this.title, BorderLayout.SOUTH);
    }

    /**
     * @return the step
     */
    public final Step getStep() {
        return this.step;
    }

    final void redraw() {
        if (this.step.isSelected()) {
            this.title.setText(String.valueOf(this.step.getTitle()).toUpperCase());
            this.order.setForeground(SELECT_STEP_LABEL_COLOR);
            this.title.setForeground(SELECT_STEP_LABEL_COLOR);
        } else {
            this.title.setText(String.valueOf(this.step.getTitle()));
            this.order.setForeground(UNSELECT_STEP_LABEL_COLOR);
            this.title.setForeground(UNSELECT_STEP_LABEL_COLOR);

        }
    }

}
