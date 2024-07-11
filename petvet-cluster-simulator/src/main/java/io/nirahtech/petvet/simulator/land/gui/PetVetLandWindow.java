package io.nirahtech.petvet.simulator.land.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class PetVetLandWindow extends JFrame {

    private final JDrawerPanel drawerPanel;
    private final JLayersPanel layersPanel;

    public PetVetLandWindow() {
        super("PetVet : Land Simulator");
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        this.drawerPanel = new JDrawerPanel();
        this.layersPanel = new JLayersPanel();

        this.layersPanel.setOnSelectedLayerEventListerner(layer -> {
            this.drawerPanel.setSelectedLayer(layer);
        });


        this.add(this.drawerPanel, BorderLayout.CENTER);
        this.add(this.layersPanel, BorderLayout.EAST);

    }
}
