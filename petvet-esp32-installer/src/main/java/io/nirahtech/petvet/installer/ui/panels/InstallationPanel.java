package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstallationPanel extends JPanel {
    
    private final JButton launchInstallBatchButton;

    public InstallationPanel() {
        super(new BorderLayout());
        this.add(new JLabel("INSTALLATION"), BorderLayout.CENTER);

        this.launchInstallBatchButton = new JButton("Install");


    }
    
}
