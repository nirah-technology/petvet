package io.nirahtech.petvet.simulator.land.gui;

import java.awt.Color;

public final class LandLayer implements Layer {
    private static final Color BORDER_COLOR = new Color(0, 200, 0);
    private static final Color BACKGROUND_COLOR = new Color(0, 128, 0, 0);

    @Override
    public Color getBorderColor() {
        return BORDER_COLOR;
    }

    @Override
    public Color getFillColor() {
        return BACKGROUND_COLOR;
    }
}
