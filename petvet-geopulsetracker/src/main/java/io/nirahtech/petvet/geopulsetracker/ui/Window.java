package io.nirahtech.petvet.geopulsetracker.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public final class Window extends JFrame {
    private final MapPanel mapPanel;
    public Window() {
        super("PETVET - Geo Pulse Tracker");
        this.setLayout(new BorderLayout());
        this.mapPanel = new MapPanel();
        this.add(mapPanel, BorderLayout.CENTER);
    }
}
