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
    private final JCartographyPanel cartographyPanel;
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
        this.cartographyPanel = new JCartographyPanel(this.cluster);
        this.nodeDetailPanel  = new NodeDetailPanel(this.cluster);
        

        this.cartographyPanel.setOnElectronicCardSelected((electronicCardSelected) -> {
            this.nodeDetailPanel.setElectronicCard(electronicCardSelected);
        });

        this.cartographyPanel.setOnElectronicCardMoved(() -> {
            this.nodeDetailPanel.updateSignalsStrenghts();
        });

        this.tabbedMenuPanel.setOnSelectionChangedEventListener(microController -> {
            this.nodeDetailPanel.setElectronicCard((ElectronicCard) microController);
            this.cartographyPanel.setSelectedMicroController(microController);
        });

        this.cartographyPanel.setEventListerOnElectronicCarCreated(electronicCard -> {
            this.tabbedMenuPanel.reload();
        });

        this.tabbedMenuPanel.addOnPlotCreatedEventListener(cadastralPlan -> {
            this.cartographyPanel.setCadastralPlan(cadastralPlan);
        });

        this.add(this.tabbedMenuPanel, BorderLayout.WEST);
        this.add(this.cartographyPanel, BorderLayout.CENTER);
        this.add(this.nodeDetailPanel, BorderLayout.EAST);
        
    }
    
}
