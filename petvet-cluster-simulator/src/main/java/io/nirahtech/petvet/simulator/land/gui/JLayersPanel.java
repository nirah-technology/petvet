package io.nirahtech.petvet.simulator.land.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;

public class JLayersPanel extends JPanel {
    private final Layer defaultLandLayer = new LandLayer();
    private final Layer defaultBuildingLayer = new BuildingLayer();

    private final List<Layer> layers = new ArrayList<>();
    private final List<JLayerPanel> layersPanels = new ArrayList<>();

    private final JButton createButton;

    private Consumer<Layer> onSelectedLayerEventListerner = null; 

    public JLayersPanel() {
        super(new BorderLayout());

        this.layers.add(defaultLandLayer);
        this.layers.add(defaultBuildingLayer);

        this.layers.forEach(layer -> {
            final JLayerPanel layerPanel = new JLayerPanel(layer);
            this.layersPanels.add(layerPanel);
        });

        final JPanel layersListPanel = new JPanel(new GridLayout(this.layersPanels.size(), 1));
        this.layersPanels.forEach(layerPanel -> {
            layersListPanel.add(layerPanel);
        });

        this.add(layersListPanel, BorderLayout.NORTH);

        this.createButton = new JButton("Create");
        this.add(this.createButton, BorderLayout.SOUTH);
        this.propagateOnSelectedLayerEventListerner();
        layersListPanel.revalidate();
        layersListPanel.repaint();

    }

    private final void propagateOnSelectedLayerEventListerner() {
        this.layersPanels.forEach(layerPanel -> {
            layerPanel.setOnSelectedLayerEventListerner(this.onSelectedLayerEventListerner);
        });
    }

    public void setOnSelectedLayerEventListerner(Consumer<Layer> onSelectedLayerEventListerner) {
        this.onSelectedLayerEventListerner = onSelectedLayerEventListerner;
        this.propagateOnSelectedLayerEventListerner();
    }

}
