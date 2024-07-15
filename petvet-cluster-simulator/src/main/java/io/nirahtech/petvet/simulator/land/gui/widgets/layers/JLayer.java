package io.nirahtech.petvet.simulator.land.gui.widgets.layers;

import java.awt.Graphics;

import javax.swing.JComponent;

public final class JLayer extends JComponent {
    
    private final Layer layer;

    public JLayer(final Layer layer) {
        this.layer = layer;
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        
    }


}
