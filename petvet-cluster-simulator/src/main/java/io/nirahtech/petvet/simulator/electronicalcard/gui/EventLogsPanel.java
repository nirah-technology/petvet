package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

public class EventLogsPanel extends JPanel {
    EventLogsPanel() {
        super(new BorderLayout());
        final Dimension dimension = new Dimension(this.getPreferredSize().width, 300);
        this.setPreferredSize(dimension); 
        this.setMinimumSize(dimension); 
        this.setMaximumSize(dimension); 
    }
}
