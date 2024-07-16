package io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers;

import java.awt.Color;

public final class BuildingLayer extends AbstractLayer {
    
    private static final Color BORDER_COLOR = new Color(200, 200, 0);
    private static final Color BACKGROUND_COLOR = new Color(128, 128, 0);

    public BuildingLayer(final int order) {
        super(order, BORDER_COLOR, BACKGROUND_COLOR);
    }
}
