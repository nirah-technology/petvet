package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.Configuration;

public class ConfigurationPanel extends JPanel {

    private final Cluster cluster;

    ConfigurationPanel(Configuration configuration, final Cluster cluster) {
        super(new BorderLayout());
        this.cluster = cluster;
        final Dimension dimension = new Dimension(200, this.getPreferredSize().height);
        this.setPreferredSize(dimension); 
        this.setMinimumSize(dimension); 
        this.setMaximumSize(dimension);

        
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Boards Cluster", null);
        tabbedPane.addTab("Pets Parc", null);
        tabbedPane.addTab("Land", null);

        // final JButton startButton = new JButton("Start");
        // final JButton stopButton = new JButton("Stop");
        // stopButton.setEnabled(false);

        // startButton.addActionListener((event) -> {
        //     this.cluster.turnOn();
        //     startButton.setEnabled(false);
        //     stopButton.setEnabled(true);
        // });

        // stopButton.addActionListener((event) -> {
        //     this.cluster.turnOff();
        //     stopButton.setEnabled(false);
        //     startButton.setEnabled(true);
        // });

        // this.add(startButton);
        // this.add(stopButton);
        this.add(tabbedPane, BorderLayout.CENTER);
    }
}
