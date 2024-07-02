package io.nirahtech.petvet.installer.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;

public class ConfigureInstallationPanel extends ResponsiveSliderPanel {
    
    ConfigureInstallationPanel(Dimension dimension) {
        super(dimension);
        this.setLayout(new BorderLayout());
        this.add(new JLabel("CONFIGURE INSTALLATION"), BorderLayout.CENTER);
    }
}
