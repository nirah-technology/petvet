package io.nirahtech.petvet.simulator.land.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class PetVetLandWindow extends JFrame {

    private final JLandDrawerPanel landDrawerPanel;

    public PetVetLandWindow() {
        super("PetVet : Land Simulator");
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.landDrawerPanel = new JLandDrawerPanel();
        this.add(this.landDrawerPanel, BorderLayout.CENTER);

    }
}
