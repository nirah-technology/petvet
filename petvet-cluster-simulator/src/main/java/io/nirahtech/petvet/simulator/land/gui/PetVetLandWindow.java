package io.nirahtech.petvet.simulator.land.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.function.Consumer;

import javax.swing.JFrame;

import io.nirahtech.petvet.simulator.land.domain.Land;

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
            this.layersPanel.setSelectedLayer(layer);

        });

        this.layersPanel.setOnLockChangeOnLayer(layer -> {
            this.drawerPanel.redraw();
        });

        this.layersPanel.setOnVisibilityChangeOnLayer(layer -> {
            this.drawerPanel.redraw();
        });


        this.add(this.drawerPanel, BorderLayout.CENTER);
        this.add(this.layersPanel, BorderLayout.EAST);

    }


    public final void setOnCadastreCreatedEventLister(Consumer<Land> onCadastreCreatedEventLister) {
        this.layersPanel.setOnCadastreCreatedEventLister(onCadastreCreatedEventLister);
    }
}
