package io.nirahtech.petvet.installer.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;

public class ResumeAfterInstallationPanel extends ResponsiveSliderPanel {
        
    ResumeAfterInstallationPanel(Dimension dimension) {
        super(dimension);
        this.setLayout(new BorderLayout());
        this.add(new JLabel("RESUME AFTER"), BorderLayout.CENTER);

    }
}
