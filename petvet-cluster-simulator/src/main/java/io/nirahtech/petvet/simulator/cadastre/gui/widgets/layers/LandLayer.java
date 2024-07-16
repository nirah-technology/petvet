package io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers;

import java.awt.Color;

public final class LandLayer extends AbstractLayer {
    private static final Color BORDER_COLOR = new Color(0, 128, 0);
    private static final Color BACKGROUND_COLOR = new Color(0, 64, 0);

    public LandLayer(final int order) {
        super(order, BORDER_COLOR, BACKGROUND_COLOR);
    }
}
