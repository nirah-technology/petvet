package io.nirahtech.petvet.simulator.land.gui;

import java.awt.Color;

public final class LandLayer extends AbstractLayer {
    private static final Color BORDER_COLOR = new Color(0, 128, 0);
    private static final Color BACKGROUND_COLOR = new Color(0, 128, 0);

    public LandLayer() {
        super(BORDER_COLOR, BACKGROUND_COLOR);
    }
}
