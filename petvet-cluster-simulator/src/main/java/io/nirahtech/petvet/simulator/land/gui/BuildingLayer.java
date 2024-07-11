package io.nirahtech.petvet.simulator.land.gui;

import java.awt.Color;

public final class BuildingLayer extends AbstractLayer {
    
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color BACKGROUND_COLOR = new Color(128, 128, 128);

    public BuildingLayer() {
        super(BORDER_COLOR, BACKGROUND_COLOR);
    }
}
