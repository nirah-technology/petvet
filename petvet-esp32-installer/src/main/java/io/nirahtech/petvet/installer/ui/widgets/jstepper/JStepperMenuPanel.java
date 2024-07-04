package io.nirahtech.petvet.installer.ui.widgets.jstepper;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

final class JStepperMenuPanel extends JPanel {

    private final List<JStepMenuItemPanel> stepsPanels = new ArrayList<>();
    
    JStepperMenuPanel() {
        super(new GridLayout(1, 0));
    }
    
    public final void addStep(final Step step) {
        final JStepMenuItemPanel stepPanel = new JStepMenuItemPanel(step);
        this.stepsPanels.add(stepPanel);
        this.setLayout(new GridLayout(1, this.stepsPanels.size()));
        this.add(stepPanel);

    }

    final void redraw() {
        this.stepsPanels.forEach(stepPanel -> stepPanel.redraw());
    }
}
