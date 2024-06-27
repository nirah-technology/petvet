package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class NodeDetailPanel extends JPanel {
    NodeDetailPanel() {
        super(new GridLayout(10, 1));
        final Dimension dimension = new Dimension(300, this.getPreferredSize().height);
        this.setPreferredSize(dimension); 
        this.setMinimumSize(dimension); 
        this.setMaximumSize(dimension); 
    }
}
