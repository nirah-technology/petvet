package io.nirahtech.petvet.installer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

public class InstallationPanel extends ResponsiveSliderPanel {
    
    InstallationPanel(Dimension dimension) {
        super(dimension);
        this.setLayout(new BorderLayout());
        this.add(new JLabel("INSTALLATION"), BorderLayout.CENTER);
        this.setBackground(new Color(255, 0, 0));
    }
}
