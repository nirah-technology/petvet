package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.Configuration;

public class PetvetClusterSimumatorWindow extends JFrame {

    private final ConfigurationPanel configurationPanel;
    private final ClusterLandPanel clusterLandPanel;
    private final NodeDetailPanel nodeDetailPanel;
    private final Cluster cluster;

    public PetvetClusterSimumatorWindow(Configuration configuration) {
        super("PETVET - Cluster Simulator");
        this.setLayout(new BorderLayout());
        try {
            this.cluster = Cluster.create(configuration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Dimension dimesion = new Dimension(1420, 720);
        this.setSize(dimesion);
        this.setMinimumSize(dimesion);
        this.setMaximumSize(dimesion);
        this.setResizable(false);

        this.configurationPanel = new ConfigurationPanel(configuration, cluster);
        this.clusterLandPanel = new ClusterLandPanel(this.cluster);
        this.nodeDetailPanel  = new NodeDetailPanel();

        this.clusterLandPanel.setOnElectronicCardSelected((electronicCardSelected) -> {
            this.nodeDetailPanel.setElectronicCard(electronicCardSelected);
        });



        this.add(this.configurationPanel, BorderLayout.WEST);
        this.add(this.clusterLandPanel, BorderLayout.CENTER);
        this.add(this.nodeDetailPanel, BorderLayout.EAST);
        
    }
    
}
