package io.nirahtech.petvet.installer.ui;

import java.awt.Dimension;

import javax.swing.JPanel;

abstract class ResponsiveSliderPanel extends JPanel {
    protected ResponsiveSliderPanel(final Dimension dimension) {
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);
        
    }
}
