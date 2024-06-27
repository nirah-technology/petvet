package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.Configuration;

public class PetvetClusterSimumatorWindow extends JFrame {

    private final ConfigurationPanel configurationPanel;
    private final ClusterLandPanel clusterLandPanel;
    private final NodeDetailPanel nodeDetailPanel;
    private final EventLogsPanel eventLogsPanel;
    private final Cluster cluster;

    public PetvetClusterSimumatorWindow(Configuration configuration) {
        super("PETVET - Cluster Simulator");
        this.setLayout(new BorderLayout());
        try {
            this.cluster = Cluster.create(configuration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.configurationPanel = new ConfigurationPanel(configuration);
        this.clusterLandPanel = new ClusterLandPanel(this.cluster);
        this.nodeDetailPanel  = new NodeDetailPanel();
        this.eventLogsPanel = new EventLogsPanel();
        this.add(this.configurationPanel, BorderLayout.WEST);
        this.add(this.clusterLandPanel, BorderLayout.CENTER);
        this.add(this.nodeDetailPanel, BorderLayout.EAST);
        this.add(this.eventLogsPanel, BorderLayout.SOUTH);
        
    }
    
}
