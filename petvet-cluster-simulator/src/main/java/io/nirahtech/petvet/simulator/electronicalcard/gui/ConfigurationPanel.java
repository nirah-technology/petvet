package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.electronicalcard.Configuration;

public class ConfigurationPanel extends JPanel {

    ConfigurationPanel(Configuration configuration) {
        super(new GridLayout(5, 1));
        final Dimension dimension = new Dimension(200, this.getPreferredSize().height);
        this.setPreferredSize(dimension); 
        this.setMinimumSize(dimension); 
        this.setMaximumSize(dimension); 
    }
}
