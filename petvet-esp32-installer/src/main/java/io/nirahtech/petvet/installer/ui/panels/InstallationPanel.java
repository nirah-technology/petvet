package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstallationPanel extends JPanel {
    
    public InstallationPanel() {
        super(new BorderLayout());
        this.add(new JLabel("INSTALLATION"), BorderLayout.CENTER);
        this.setBackground(new Color(255, 0, 0));

    }
    
}
