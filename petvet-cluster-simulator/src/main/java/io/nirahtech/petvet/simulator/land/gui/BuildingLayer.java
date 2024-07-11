package io.nirahtech.petvet.simulator.land.gui;

import java.awt.Color;

public final class BuildingLayer implements Layer {
    
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color BACKGROUND_COLOR = new Color(128, 128, 128, 0);

    @Override
    public Color getBorderColor() {
        return BORDER_COLOR;
    }

    @Override
    public Color getFillColor() {
        return BACKGROUND_COLOR;
    }
}
