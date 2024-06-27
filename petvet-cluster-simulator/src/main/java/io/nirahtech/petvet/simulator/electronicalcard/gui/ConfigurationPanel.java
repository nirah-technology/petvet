package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.Configuration;

public class ConfigurationPanel extends JPanel {

    private final Cluster cluster;

    ConfigurationPanel(Configuration configuration, final Cluster cluster) {
        super(new GridLayout(5, 1));
        this.cluster = cluster;
        final Dimension dimension = new Dimension(200, this.getPreferredSize().height);
        this.setPreferredSize(dimension); 
        this.setMinimumSize(dimension); 
        this.setMaximumSize(dimension);
    
        final JButton startButton = new JButton("Start");
        final JButton stopButton = new JButton("Stop");
        stopButton.setEnabled(false);

        startButton.addActionListener((event) -> {
            this.cluster.turnOn();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });

        stopButton.addActionListener((event) -> {
            this.cluster.turnOff();
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        });

        this.add(startButton);
        this.add(stopButton);
    }
}
