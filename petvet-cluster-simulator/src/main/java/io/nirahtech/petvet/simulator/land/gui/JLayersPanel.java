package io.nirahtech.petvet.simulator.land.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.land.domain.Building;
import io.nirahtech.petvet.simulator.land.domain.Land;
import io.nirahtech.petvet.simulator.land.domain.Segment;

public class JLayersPanel extends JPanel {

    private final SortedSet<Layer> layers = new TreeSet<>(Comparator.comparingInt(Layer::getOrder));
    private final List<JLayerPanel> layersPanels = new ArrayList<>();

    private final AtomicReference<Layer> selectedLayer = new AtomicReference<>(null);

    private final JButton createButton;

    private Consumer<Layer> onSelectedLayerEventListerner = null;
    private Consumer<Layer> onLockChangedOnLayerEventListener = null;
    private Consumer<Layer> onVisibilityChangedOnLayerEventListener = null;
    private Consumer<Land> onCadastreCreatedEventLister = null;

    public JLayersPanel() {
        super(new BorderLayout());

        this.layers.add(new LandLayer(1));
        this.layers.add(new BuildingLayer(2));

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

        this.createButton.addActionListener(event -> {
            final List<Segment> landSegments = new ArrayList<>();
            final List<List<Segment>> buildingsSegments = new ArrayList<>();
            final List<Building> buildings = new ArrayList<>();

            for (Layer layer : layers) {
                List<Segment> segments = createSegments(layer.getPoints());

                if (layer instanceof LandLayer) {
                    landSegments.addAll(segments);
                } else if (layer instanceof BuildingLayer) {
                    buildingsSegments.add(segments);
                    buildings.add(new Building(segments.toArray(new Segment[0])));
                }
            }

            final Land land = new Land(
                    landSegments.toArray(new Segment[0]),
                    buildings.toArray(new Building[0]));

            if (Objects.nonNull(onCadastreCreatedEventLister)) {
                onCadastreCreatedEventLister.accept(land);
            }
        });

        this.add(this.createButton, BorderLayout.SOUTH);
        this.propagateOnSelectedLayerEventListerner();
        layersListPanel.revalidate();
        layersListPanel.repaint();

    }

    private List<Segment> createSegments(List<Point> points) {
        List<Segment> segments = new ArrayList<>();
        int size = points.size();

        for (int index = 0; index < size; index++) {
            Point from = points.get(index);
            Point to = points.get((index + 1) % size);
            segments.add(new Segment(from, to));
        }

        return segments;
    }

    private final void propagateOnSelectedLayerEventListerner() {
        this.layersPanels.forEach(layerPanel -> {
            layerPanel.setOnSelectedLayerEventListerner(this.onSelectedLayerEventListerner);
            layerPanel.setOnLockChangedOnLayerEventListener(this.onLockChangedOnLayerEventListener);
            layerPanel.setOnVisibilityChangedOnLayerEventListener(this.onVisibilityChangedOnLayerEventListener);
        });
    }

    public void setOnSelectedLayerEventListerner(Consumer<Layer> onSelectedLayerEventListerner) {
        this.onSelectedLayerEventListerner = onSelectedLayerEventListerner;
        this.propagateOnSelectedLayerEventListerner();
    }

    public void setOnLockChangeOnLayer(Consumer<Layer> onLockChangedOnLayerEventListener) {
        this.onLockChangedOnLayerEventListener = onLockChangedOnLayerEventListener;
        this.propagateOnSelectedLayerEventListerner();
    }

    public void setOnVisibilityChangeOnLayer(Consumer<Layer> onVisibilityChangedOnLayerEventListener) {
        this.onVisibilityChangedOnLayerEventListener = onVisibilityChangedOnLayerEventListener;
        this.propagateOnSelectedLayerEventListerner();
    }

    public void setSelectedLayer(Layer layer) {
        this.selectedLayer.set(layer);
        this.layersPanels.forEach(layerPanel -> {
            layerPanel.setSelectedLayer(layer);
        });
        this.revalidate();
        this.repaint();
    }

    public final void setOnCadastreCreatedEventLister(Consumer<Land> onCadastreCreatedEventLister) {
        this.onCadastreCreatedEventLister = onCadastreCreatedEventLister;
    }

}
