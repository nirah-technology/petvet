package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;

import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.Configuration;
import io.nirahtech.petvet.simulator.electronicalcard.ElectronicCard;
import io.nirahtech.petvet.simulator.electronicalcard.gui.leftpanel.JTabbedMenuPanel;

public class PetvetClusterSimumatorWindow extends JFrame {

    private final JTabbedMenuPanel tabbedMenuPanel;
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
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        this.tabbedMenuPanel = new JTabbedMenuPanel(configuration, cluster);
        this.clusterLandPanel = new ClusterLandPanel(this.cluster);
        this.nodeDetailPanel  = new NodeDetailPanel(this.cluster);
        

        this.clusterLandPanel.setOnElectronicCardSelected((electronicCardSelected) -> {
            this.nodeDetailPanel.setElectronicCard(electronicCardSelected);
        });

        this.clusterLandPanel.setOnElectronicCardMoved(() -> {
            this.nodeDetailPanel.updateSignalsStrenghts();
        });

        this.tabbedMenuPanel.setOnSelectionChangedEventListener(microController -> {
            this.nodeDetailPanel.setElectronicCard((ElectronicCard) microController);
            this.clusterLandPanel.setSelectedMicroController(microController);
        });

        this.clusterLandPanel.setEventListerOnElectronicCarCreated(electronicCard -> {
            this.tabbedMenuPanel.reload();
        });

        this.cl

        this.add(this.tabbedMenuPanel, BorderLayout.WEST);
        this.add(this.clusterLandPanel, BorderLayout.CENTER);
        this.add(this.nodeDetailPanel, BorderLayout.EAST);
        
    }
    
}
